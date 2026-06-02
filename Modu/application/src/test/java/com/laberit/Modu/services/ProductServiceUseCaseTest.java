package com.laberit.Modu.services;

import com.laberit.Modu.domain.exceptions.ProductNotFoundException;
import com.laberit.Modu.domain.model.*;
import com.laberit.Modu.domain.model.response.PagedResult;
import com.laberit.Modu.ports.driven.CategoryRepositoryPort;
import com.laberit.Modu.ports.driven.ProductCategoryRepositoryPort;
import com.laberit.Modu.ports.driven.ProductRepositoryPort;
import com.laberit.Modu.ports.driving.ProductVariantServicePort;
import com.laberit.Modu.ports.driving.command.SearchProductsCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceUseCaseTest {

    @Mock private ProductRepositoryPort productRepositoryPort;
    @Mock private CategoryRepositoryPort categoryRepositoryPort;
    @Mock private ProductCategoryRepositoryPort productCategoryRepositoryPort;
    @Mock private ProductVariantServicePort productVariantServicePort;

    @InjectMocks
    private ProductServiceUseCase productService;

    // ── findProductById ───────────────────────────────────────────────────────

    @Test
    void findProductById_shouldReturnProductWithCategoriesAndVariants_whenProductExists() {
        // Given
        Long productId = 1L;
        Product mockProduct = Product.builder().id(productId).name("Test Product").build();

        List<ProductCategory> productCategories = List.of(
                new ProductCategory(null,  10, productId),
                new ProductCategory(null,  20, productId)
        );
        Set<Category> categories = Set.of(
                Category.builder().id(10).name("Electronics").build(),
                Category.builder().id(20).name("Gadgets").build()
        );
        List<ProductVariant> variants = List.of(
                ProductVariant.builder().id(1L).productId(productId).build()
        );

        when(productRepositoryPort.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(productCategoryRepositoryPort.findAllByProductId(productId)).thenReturn(productCategories);
        when(categoryRepositoryPort.findAllByIdIn(Set.of(10, 20))).thenReturn(categories);
        when(productVariantServicePort.findAllByProductId(productId)).thenReturn(variants);

        // When
        Product result = productService.findProductById(productId);

        // Then
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals(categories, result.getCategoriesSet());
        assertEquals(variants, result.getProductVariantsList());

        verify(productRepositoryPort).findById(productId);
        verify(productCategoryRepositoryPort).findAllByProductId(productId);
        verify(categoryRepositoryPort).findAllByIdIn(Set.of(10, 20));
        verify(productVariantServicePort).findAllByProductId(productId);
    }

    @Test
    void findProductById_shouldThrowProductNotFoundException_whenProductDoesNotExist() {
        // given
        Long productId = 99L;
        when(productRepositoryPort.findById(productId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ProductNotFoundException.class,
                () -> productService.findProductById(productId));

        verify(productRepositoryPort).findById(productId);
        verifyNoInteractions(productCategoryRepositoryPort, categoryRepositoryPort, productVariantServicePort);
    }

    @Test
    void findProductById_shouldReturnProductWithEmptyCategories_whenProductHasNoCategories() {
        // given
        Long productId = 1L;
        Product mockProduct = Product.builder().id(productId).build();

        when(productRepositoryPort.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(productCategoryRepositoryPort.findAllByProductId(productId)).thenReturn(List.of());
        when(categoryRepositoryPort.findAllByIdIn(Set.of())).thenReturn(Set.of());
        when(productVariantServicePort.findAllByProductId(productId)).thenReturn(List.of());

        // when
        Product result = productService.findProductById(productId);

        // then
        assertNotNull(result);
        assertTrue(result.getCategoriesSet().isEmpty());
        assertTrue(result.getProductVariantsList().isEmpty());
    }

    // ── search ────────────────────────────────────────────────────────────────

    @Test
    void search_shouldSortByPriceAsc_whenOrderByPriceIsAsc() {
        // given
        SearchProductsCommand command = new SearchProductsCommand(
                null, "ASC", null, null, 0, 10
        );
        PagedResult<Product> mockResult = new PagedResult<>(List.of(), 0, 10, false);
        when(productRepositoryPort.findAll(any(ProductSearchCriteria.class))).thenReturn(mockResult);

        // when
        productService.search(command);

        // then
        ArgumentCaptor<ProductSearchCriteria> captor =
                ArgumentCaptor.forClass(ProductSearchCriteria.class);
        verify(productRepositoryPort).findAll(captor.capture());

        ProductSearchCriteria captured = captor.getValue();
        assertEquals(ProductSortField.PRICE, captured.sortField());
        assertEquals(SortDirection.ASC, captured.sortDirection());
    }

    @Test
    void search_shouldSortByPriceDesc_whenOrderByPriceIsDesc() {
        // Arrange
        SearchProductsCommand command = new SearchProductsCommand(
                null, "DESC", null, null, 0, 10
        );
        when(productRepositoryPort.findAll(any(ProductSearchCriteria.class)))
                .thenReturn(new PagedResult<>(List.of(), 0, 10, false));

        // Act
        productService.search(command);

        // Assert
        ArgumentCaptor<ProductSearchCriteria> captor =
                ArgumentCaptor.forClass(ProductSearchCriteria.class);
        verify(productRepositoryPort).findAll(captor.capture());

        assertEquals(SortDirection.DESC, captor.getValue().sortDirection());
        assertEquals(ProductSortField.PRICE, captor.getValue().sortField());
    }

    @Test
    void search_shouldSortByIdDesc_whenOrderByPriceIsNull() {
        // Arrange
        SearchProductsCommand command = new SearchProductsCommand(
                "laptop", null, null, null, 0, 10
        );
        when(productRepositoryPort.findAll(any(ProductSearchCriteria.class)))
                .thenReturn(new PagedResult<>(List.of(), 0, 10, false));

        // Act
        productService.search(command);

        // Assert
        ArgumentCaptor<ProductSearchCriteria> captor =
                ArgumentCaptor.forClass(ProductSearchCriteria.class);
        verify(productRepositoryPort).findAll(captor.capture());

        assertEquals(ProductSortField.ID, captor.getValue().sortField());
        assertEquals(SortDirection.DESC, captor.getValue().sortDirection());
    }

    @Test
    void search_shouldPassAllCriteriaFieldsThrough() {
        // given
        List<String> categories = List.of("Electronics", "Gadgets", "Consumables");

        SearchProductsCommand command = new SearchProductsCommand(
                "laptop", "ASC", 999, categories, 2, 5
        );

        when(productRepositoryPort.findAll(any(ProductSearchCriteria.class)))
                .thenReturn(new PagedResult<>(List.of(), 0, 2, false));

        // when
        productService.search(command);

        // then
        ArgumentCaptor<ProductSearchCriteria> captor =
                ArgumentCaptor.forClass(ProductSearchCriteria.class);
        verify(productRepositoryPort).findAll(captor.capture());

        ProductSearchCriteria captured = captor.getValue();

        assertEquals("laptop", captured.title());
        assertEquals(999, captured.maxPrice());
        assertEquals(categories, captured.categories());
        assertEquals(2, captured.page());
        assertEquals(5, captured.size());
    }
}