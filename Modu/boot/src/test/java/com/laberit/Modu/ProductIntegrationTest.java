package com.laberit.Modu;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@Testcontainers
class ProductIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shouldReturnPagedProducts_withPaginationMeta() {
        ResponseEntity<String> response = restTemplate.getForEntity("/products", String.class);
        DocumentContext json = JsonPath.parse(response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat((List<?>) json.read("$.data")).isNotEmpty();
        assertThat(json.read("$.meta.page", Integer.class)).isEqualTo(0);
        assertThat(json.read("$.meta.size", Integer.class)).isGreaterThan(0);
        assertThat(json.read("$.meta.has_next", Boolean.class)).isNotNull();
    }

    @Test
    void shouldReturnFewerProducts_whenFilteredByMaxPrice() {
        // Request all products (large size to count them)
        ResponseEntity<String> allResponse = restTemplate.getForEntity("/products?size=100", String.class);
        // Request only products priced at or below 20 — seed data includes White T-Shirt (19.99), Knitted Beanie (14.99), Slide Sandals (19.99)
        ResponseEntity<String> filteredResponse = restTemplate.getForEntity("/products?size=100&maxPrice=20", String.class);

        int totalCount    = ((List<?>) JsonPath.parse(allResponse.getBody()).read("$.data")).size();
        int filteredCount = ((List<?>) JsonPath.parse(filteredResponse.getBody()).read("$.data")).size();

        assertThat(filteredResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(filteredCount).isGreaterThan(0);
        assertThat(filteredCount).isLessThan(totalCount);
    }

    @Test
    void shouldReturnMatchingProducts_whenFilteredByTitle() {
        // seed data has "Classic Denim Jacket" (id=1), "Denim Shorts" (id=15), "Denim Western Shirt" (id=27), etc.
        ResponseEntity<String> response = restTemplate.getForEntity("/products?title=Denim", String.class);
        DocumentContext json = JsonPath.parse(response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Integer> productIds = json.read("$.data[*].product_id");
        assertThat(productIds).isNotEmpty();
        assertThat(productIds).contains(1); // Classic Denim Jacket
    }

    @Test
    void shouldReturnProductDetails_withVariantsAndCategories() {
        // Product 1: "Classic Denim Jacket", price=79.99, categories: Streetwear + Vintage, 25 variants
        ResponseEntity<String> response = restTemplate.getForEntity("/products/1", String.class);
        DocumentContext json = JsonPath.parse(response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(json.read("$.id", Long.class)).isEqualTo(1L);
        assertThat(json.read("$.name", String.class)).isEqualTo("Classic Denim Jacket");
        assertThat(json.read("$.price", Double.class)).isEqualTo(79.99);
        assertThat(json.read("$.active", Boolean.class)).isTrue();
        assertThat((List<?>) json.read("$.productVariantsList")).isNotEmpty();
        assertThat((List<?>) json.read("$.categoriesSet")).isNotEmpty();
    }

    @Test
    void shouldReturnCorrectVariantDetails_forProductById() {
        // Product 1, variant 1_S_BLUE: size=S, color=BLUE, active=true, productId=1
        ResponseEntity<String> response = restTemplate.getForEntity("/products/1", String.class);
        DocumentContext json = JsonPath.parse(response.getBody());

        List<String> variantNames = json.read("$.productVariantsList[*].name");
        assertThat(variantNames).contains("1_S_BLUE");

        // Verify a known variant's fields
        int idx = variantNames.indexOf("1_S_BLUE");
        assertThat(json.read("$.productVariantsList[" + idx + "].size", String.class)).isEqualTo("S");
        assertThat(json.read("$.productVariantsList[" + idx + "].color", String.class)).isEqualTo("BLUE");
        assertThat(json.read("$.productVariantsList[" + idx + "].productId", Long.class)).isEqualTo(1L);
    }
}
