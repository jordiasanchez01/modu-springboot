package com.laberit.Modu.rest.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laberit.Modu.domain.exceptions.ProductNotFoundException;
import com.laberit.Modu.domain.model.Product;
import com.laberit.Modu.domain.model.response.PagedResult;
import com.laberit.Modu.ports.driving.ProductServicePort;
import com.laberit.Modu.ports.driving.command.SearchProductsCommand;
import com.laberit.Modu.rest.generated.model.ProductDetailsResponse;
import com.laberit.Modu.rest.generated.model.ProductPageResponse;
import com.laberit.Modu.rest.mapper.ProductRestMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductRestAdapter.class)
class ProductRestAdapterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductServicePort productServicePort;

    @MockitoBean
    private ProductRestMapper mapper;

    @MockitoBean
    private ObjectMapper objectMapper;

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
                .andExpect((ResultMatcher) jsonPath("$.id").value(1L))
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
        PagedResult<Product> mockPagedResult = new PagedResult<>(List.of(), 0, 0, 10);
        ProductPageResponse mockResponse = new ProductPageResponse(
                .content(List.of())
                .totalElements(0L));

        when(productServicePort.search(any(SearchProductsCommand.class)))
                .thenReturn(mockPagedResult);
        when(mapper.toProductPageResponse(mockPagedResult)).thenReturn(mockResponse);

        mockMvc.perform(get("/products")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void getProducts_shouldPassAllQueryParamsToCommand() throws Exception {
        when(productServicePort.search(any(SearchProductsCommand.class)))
                .thenReturn(new PagedResult<>(List.of(), 0, 0, 10));
        when(mapper.toProductPageResponse(any())).thenReturn(new ProductPageResponse());

        mockMvc.perform(get("/products")
                        .param("page", "0")
                        .param("size", "10")
                        .param("title", "laptop")
                        .param("orderByPrice", "ASC")
                        .param("maxPrice", "999"))
                .andExpect(status().isOk());

        ArgumentCaptor<SearchProductsCommand> captor =
                ArgumentCaptor.forClass(SearchProductsCommand.class);
        verify(productServicePort).search(captor.capture());

        SearchProductsCommand captured = captor.getValue();
        assertEquals("laptop", captured.title());
        assertEquals("ASC", captured.orderByPrice());
        assertEquals(999, captured.maxPrice());
        assertEquals(0, captured.page());
        assertEquals(10, captured.size());
    }

    @Test
    void getProducts_shouldReturn200_withNullOptionalParams() throws Exception {
        when(productServicePort.search(any(SearchProductsCommand.class)))
                .thenReturn(new PagedResult<>(List.of(), 0, 0, 10));
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