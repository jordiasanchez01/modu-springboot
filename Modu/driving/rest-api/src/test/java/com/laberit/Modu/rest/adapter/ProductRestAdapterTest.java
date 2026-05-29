package com.laberit.Modu.rest.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laberit.Modu.TestApplication;
import com.laberit.Modu.domain.exceptions.ProductNotFoundException;
import com.laberit.Modu.domain.model.Product;
import com.laberit.Modu.domain.model.response.PagedResult;
import com.laberit.Modu.ports.driving.ProductServicePort;
import com.laberit.Modu.ports.driving.command.SearchProductsCommand;
import com.laberit.Modu.rest.generated.model.ProductDetailsResponse;
import com.laberit.Modu.rest.generated.model.ProductPageResponse;
import com.laberit.Modu.rest.generated.model.ProductsResponse;
import com.laberit.Modu.rest.mapper.ProductRestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = ProductRestAdapter.class
)
@ContextConfiguration(classes = TestApplication.class)
class ProductRestAdapterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductServicePort productServicePort;

    @MockitoBean
    private ProductRestMapper mapper;

    @MockitoBean
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationEntryPoint authenticationEntryPoint;

    private List<Product> mockProducts;
    private List<ProductsResponse> mockProductsResponse;
    private ProductPageResponse mockPageResponse;

    @BeforeEach
    void setUp() {
        mockProducts = List.of(
                Product.builder().id(1L).name("Laptop").imageUrl("productImgUrl1").build(),
                Product.builder().id(2L).name("Phone").imageUrl("productImgUrl2").build(),
                Product.builder().id(3L).name("Tablet").imageUrl("productImgUrl3").build(),
                Product.builder().id(4L).name("Smartwatch").imageUrl("productImgUrl4").build(),
                Product.builder().id(5L).name("Printer").imageUrl("productImgUrl5").build()
        );

        mockProductsResponse = mockProducts.stream()
                .map(mapper::toProductsResponse)
                .collect(Collectors.toList());

        mockPageResponse = new ProductPageResponse()
                .data(mockProductsResponse);
    }

    // ── getProductById ────────────────────────────────────────────────────────

    @Test
    void getProductById_shouldReturn200_withProductDetails_whenProductExists() throws Exception {
        Product mockProduct = Product.builder().id(1L).name("Test Product").build();
        ProductDetailsResponse mockResponse = new ProductDetailsResponse()
                .id(1L)
                .name("Test Product");

        when(productServicePort.findProductById(1L)).thenReturn(mockProduct);
        when(mapper.toProductDetailsResponse(mockProduct)).thenReturn(mockResponse);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void getProductById_shouldReturn404_whenProductDoesNotExist() throws Exception {
        when(productServicePort.findProductById(99L))
                .thenThrow(new ProductNotFoundException("99"));

        mockMvc.perform(get("/products/99"))
                .andExpect(status().isNotFound());
    }

    // ── getProducts ───────────────────────────────────────────────────────────

    @Test
    void getProducts_shouldReturn200_withPagedResults() throws Exception {
        PagedResult<Product> mockPagedResult = new PagedResult<>(mockProducts, 0, 5, false);

        when(productServicePort.search(any(SearchProductsCommand.class)))
                .thenReturn(mockPagedResult);
        when(mapper.toProductPageResponse(mockPagedResult))
                .thenReturn(mockPageResponse);

        mockMvc.perform(get("/products")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(5));
    }

    @Test
    void getProducts_shouldPassAllQueryParamsToCommand() throws Exception {
        when(productServicePort.search(any(SearchProductsCommand.class)))
                .thenReturn(new PagedResult<>(List.of(), 0, 10, false));
        when(mapper.toProductPageResponse(any())).thenReturn(new ProductPageResponse());

        mockMvc.perform(get("/products")
                        .param("page", "0")
                        .param("size", "10")
                        .param("title", "laptop")
                        .param("orderByPrice", "asc")
                        .param("maxPrice", "999"))
                .andExpect(status().isOk());

        ArgumentCaptor<SearchProductsCommand> captor =
                ArgumentCaptor.forClass(SearchProductsCommand.class);
        verify(productServicePort).search(captor.capture());

        SearchProductsCommand captured = captor.getValue();
        assertEquals("laptop", captured.title());
        assertEquals("asc", captured.orderByPrice());
        assertEquals(999, captured.maxPrice());
        assertEquals(0, captured.page());
        assertEquals(10, captured.size());
    }

    @Test
    void getProducts_shouldReturn200_withNullOptionalParams() throws Exception {
        when(productServicePort.search(any(SearchProductsCommand.class)))
                .thenReturn(new PagedResult<>(List.of(), 0, 0, false));
        when(mapper.toProductPageResponse(any())).thenReturn(new ProductPageResponse());

        mockMvc.perform(get("/products")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        ArgumentCaptor<SearchProductsCommand> captor =
                ArgumentCaptor.forClass(SearchProductsCommand.class);
        verify(productServicePort).search(captor.capture());

        assertNull(captor.getValue().title());
        assertNull(captor.getValue().orderByPrice());
        assertNull(captor.getValue().maxPrice());
    }
}