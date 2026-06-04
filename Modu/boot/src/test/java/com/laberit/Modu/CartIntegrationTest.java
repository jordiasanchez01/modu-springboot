package com.laberit.Modu;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
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
class CartIntegrationTest {

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

    private static final String DEVICE_INITIALIZE = "CART0000000000000000000000000001";
    private static final String DEVICE_GET_CART   = "CART0000000000000000000000000002";

    private HttpHeaders headersWithDevice(String deviceId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", deviceId);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Test
    void shouldReturnUnauthorized_whenNoAuthorizationHeader() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/cart", HttpMethod.GET, HttpEntity.EMPTY, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldCreateEmptyCart_whenInitialized() {
        HttpEntity<Void> request = new HttpEntity<>(headersWithDevice(DEVICE_INITIALIZE));

        ResponseEntity<String> response = restTemplate.exchange("/cart", HttpMethod.PUT, request, String.class);
        DocumentContext json = JsonPath.parse(response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(json.read("$.deviceId", String.class)).isEqualTo(DEVICE_INITIALIZE);
        assertThat((List<?>) json.read("$.cartItems")).isEmpty();
        assertThat(json.read("$.subTotalPrice", Double.class)).isEqualTo(0.0);
        assertThat(json.read("$.totalPrice", Double.class)).isEqualTo(0.0);
    }

    @Test
    void shouldReturnCart_whenFetchedAfterInitialization() {
        HttpEntity<Void> request = new HttpEntity<>(headersWithDevice(DEVICE_GET_CART));
        restTemplate.exchange("/cart", HttpMethod.PUT, request, String.class);

        ResponseEntity<String> response = restTemplate.exchange("/cart", HttpMethod.GET, request, String.class);
        DocumentContext json = JsonPath.parse(response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(json.read("$.cartSummary.deviceId", String.class)).isEqualTo(DEVICE_GET_CART);
        assertThat((List<?>) json.read("$.cartSummary.cartItems")).isEmpty();
    }
}
