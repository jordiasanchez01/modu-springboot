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
class CartItemIntegrationTest {

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

    // 32-character device IDs (DeviceIdFilter accepts length 16 or 32)
    private static final String DEVICE_ADD_ITEM   = "00000000000000000000000000000001";
    private static final String DEVICE_DELETE_ONE = "00000000000000000000000000000002";
    private static final String DEVICE_DELETE_ALL = "00000000000000000000000000000003";

    private HttpHeaders headersWithDevice(String deviceId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", deviceId);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private void initializeCart(String deviceId) {
        HttpEntity<Void> request = new HttpEntity<>(headersWithDevice(deviceId));
        restTemplate.exchange("/cart", HttpMethod.PUT, request, String.class);
    }

    @Test
    void shouldReturnUnauthorized_whenNoAuthorizationHeader() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/cart/addItem", HttpMethod.POST, HttpEntity.EMPTY, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldAddCartItemToExistingCart() {
        initializeCart(DEVICE_ADD_ITEM);
        // variant 1 (1_S_BLUE): active=true, stock=7, product_id=1 (price 79.99)
        String body = "{\"variant_id\": 1, \"quantity\": 2}";
        HttpEntity<String> request = new HttpEntity<>(body, headersWithDevice(DEVICE_ADD_ITEM));

        ResponseEntity<String> response = restTemplate.exchange("/cart/addItem", HttpMethod.POST, request, String.class);
        DocumentContext json = JsonPath.parse(response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat((List<?>) json.read("$.cartItems")).hasSize(1);
        assertThat(json.read("$.cartItems[0].productVariantId", Long.class)).isEqualTo(1L);
        assertThat(json.read("$.cartItems[0].quantity", Integer.class)).isEqualTo(2);
    }

    @Test
    void shouldDeleteSingleCartItem() {
        initializeCart(DEVICE_DELETE_ONE);

        String addBody = "{\"variant_id\": 1, \"quantity\": 1}";
        HttpEntity<String> addRequest = new HttpEntity<>(addBody, headersWithDevice(DEVICE_DELETE_ONE));
        ResponseEntity<String> addResponse = restTemplate.exchange("/cart/addItem", HttpMethod.POST, addRequest, String.class);
        Long itemId = JsonPath.parse(addResponse.getBody()).read("$.cartItems[0].id", Long.class);

        HttpEntity<Void> deleteRequest = new HttpEntity<>(headersWithDevice(DEVICE_DELETE_ONE));
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
                "/cart/items/" + itemId, HttpMethod.DELETE, deleteRequest, String.class);
        DocumentContext json = JsonPath.parse(deleteResponse.getBody());

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat((List<?>) json.read("$.cartItems")).isEmpty();
    }

    @Test
    void shouldDeleteAllCartItems() {
        initializeCart(DEVICE_DELETE_ALL);

        String addBody = "{\"variant_id\": 1, \"quantity\": 1}";
        HttpEntity<String> addRequest = new HttpEntity<>(addBody, headersWithDevice(DEVICE_DELETE_ALL));
        restTemplate.exchange("/cart/addItem", HttpMethod.POST, addRequest, String.class);

        HttpEntity<Void> deleteRequest = new HttpEntity<>(headersWithDevice(DEVICE_DELETE_ALL));
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
                "/cart/items", HttpMethod.DELETE, deleteRequest, String.class);
        DocumentContext json = JsonPath.parse(deleteResponse.getBody());

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat((List<?>) json.read("$.cartItems")).isEmpty();
    }
}
