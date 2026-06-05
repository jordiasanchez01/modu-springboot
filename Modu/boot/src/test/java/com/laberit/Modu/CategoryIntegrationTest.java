package com.laberit.Modu;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import com.laberit.Modu.domain.model.Category;
import com.laberit.Modu.ports.driven.CategoryRepositoryPort;
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
class CategoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    CategoryRepositoryPort categoryRepositoryPort;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shouldReturnCategories() {
        // given
        List<String> expectedCategoriesNames = categoryRepositoryPort.findAll()
                .stream()
                .map(Category::name)
                .toList();

        // when
        ResponseEntity<String> response = restTemplate.getForEntity("/categories", String.class);
        DocumentContext json = JsonPath.parse(response.getBody());

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(json.read("$.length()", Integer.class)).isEqualTo(expectedCategoriesNames.size());
        assertThat(json.read("$[0].name", String.class)).isNotBlank();
        assertThat(json.read("$[*].name", List.class)).containsExactlyInAnyOrderElementsOf(expectedCategoriesNames);
    }
}
