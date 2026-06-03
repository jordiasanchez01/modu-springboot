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
class OrderIntegrationTest {

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

    // variant 1 (1_S_BLUE): active=true, stock=7, product_id=1 (price 79.99)
    private static final String DEVICE_CHECKOUT   = "ORDER00000000000000000000000000001";
    private static final String DEVICE_GET_ORDER  = "ORDER00000000000000000000000000002";
    private static final String DEVICE_EMPTY_CART = "ORDER00000000000000000000000000003";

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

    private DocumentContext addItemToCart(String deviceId) {
        String body = "{\"variant_id\": 1, \"quantity\": 1}";
        HttpEntity<String> request = new HttpEntity<>(body, headersWithDevice(deviceId));
        ResponseEntity<String> response = restTemplate.exchange("/cart/addItem", HttpMethod.POST, request, String.class);
        return JsonPath.parse(response.getBody());
    }

    private String buildCheckoutBody(DocumentContext cartJson) {
        Long itemId      = cartJson.read("$.cartItems[0].id", Long.class);
        Long productId   = cartJson.read("$.cartItems[0].productId", Long.class);
        Long variantId   = cartJson.read("$.cartItems[0].productVariantId", Long.class);
        Integer quantity = cartJson.read("$.cartItems[0].quantity", Integer.class);
        Double unitPrice = cartJson.read("$.cartItems[0].unitPrice", Double.class);
        Double totalPrice = cartJson.read("$.cartItems[0].totalPrice", Double.class);

        return String.format(
                "{\"isPaid\":true,\"specialInstructions\":\"none\",\"shippingCosts\":0.0," +
                "\"cartToOrder\":{\"cart_items\":[{\"id\":%d,\"productId\":%d,\"productVariantId\":%d," +
                "\"quantity\":%d,\"unitPrice\":%.2f,\"totalPrice\":%.2f}]}}",
                itemId, productId, variantId, quantity, unitPrice, totalPrice);
    }

    @Test
    void shouldReturnUnauthorized_whenNoAuthorizationHeader() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/checkout", HttpMethod.POST, HttpEntity.EMPTY, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldCreateOrder_whenCheckingOutCartWithItems() {
        initializeCart(DEVICE_CHECKOUT);
        DocumentContext cartJson = addItemToCart(DEVICE_CHECKOUT);

        HttpEntity<String> request = new HttpEntity<>(buildCheckoutBody(cartJson), headersWithDevice(DEVICE_CHECKOUT));
        ResponseEntity<String> response = restTemplate.exchange("/checkout", HttpMethod.POST, request, String.class);
        DocumentContext json = JsonPath.parse(response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(json.read("$.orderPlaced", Boolean.class)).isTrue();
        assertThat(json.read("$.orderId", Long.class)).isGreaterThan(0L);
        assertThat(json.read("$.order.deviceId", String.class)).isEqualTo(DEVICE_CHECKOUT);
        assertThat((List<?>) json.read("$.order.order_items")).hasSize(1);
    }

    @Test
    void shouldReturnOrderById_withOrderItems() {
        initializeCart(DEVICE_GET_ORDER);
        DocumentContext cartJson = addItemToCart(DEVICE_GET_ORDER);

        HttpEntity<String> checkoutRequest = new HttpEntity<>(buildCheckoutBody(cartJson), headersWithDevice(DEVICE_GET_ORDER));
        ResponseEntity<String> checkoutResponse = restTemplate.exchange("/checkout", HttpMethod.POST, checkoutRequest, String.class);
        Long orderId = JsonPath.parse(checkoutResponse.getBody()).read("$.orderId", Long.class);

        HttpEntity<Void> getRequest = new HttpEntity<>(headersWithDevice(DEVICE_GET_ORDER));
        ResponseEntity<String> orderResponse = restTemplate.exchange(
                "/get_order/" + orderId, HttpMethod.GET, getRequest, String.class);
        DocumentContext json = JsonPath.parse(orderResponse.getBody());

        assertThat(orderResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(json.read("$.id", Long.class)).isEqualTo(orderId);
        assertThat(json.read("$.deviceId", String.class)).isEqualTo(DEVICE_GET_ORDER);
        assertThat((List<?>) json.read("$.order_items")).isNotEmpty();
        assertThat(json.read("$.order_items[0].productVariantId", Long.class)).isEqualTo(1L);
        assertThat(json.read("$.order_items[0].quantity", Integer.class)).isEqualTo(1);
    }

    @Test
    void shouldReturnConflict_whenCheckingOutEmptyCart() {
        initializeCart(DEVICE_EMPTY_CART);

        String emptyCheckoutBody = "{\"isPaid\":true,\"specialInstructions\":\"\",\"shippingCosts\":0.0," +
                "\"cartToOrder\":{\"cart_items\":[]}}";
        HttpEntity<String> request = new HttpEntity<>(emptyCheckoutBody, headersWithDevice(DEVICE_EMPTY_CART));
        ResponseEntity<String> response = restTemplate.exchange("/checkout", HttpMethod.POST, request, String.class);
        DocumentContext json = JsonPath.parse(response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(json.read("$.orderPlaced", Boolean.class)).isFalse();
    }
}
