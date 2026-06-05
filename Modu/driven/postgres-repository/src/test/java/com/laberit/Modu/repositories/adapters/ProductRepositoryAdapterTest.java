package com.laberit.Modu.repositories.adapters;

import com.laberit.Modu.domain.model.Product;
import com.laberit.Modu.domain.model.ProductSearchCriteria;
import com.laberit.Modu.domain.model.ProductSortField;
import com.laberit.Modu.domain.model.SortDirection;
import com.laberit.Modu.domain.model.response.PagedResult;
import com.laberit.Modu.repositories.ProductJpaRepository;
import com.laberit.Modu.repositories.mappers.ProductPersistanceMapper;
import com.laberit.Modu.repositories.models.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryAdapterTest {

    @Mock private ProductJpaRepository productJpaRepository;
    @Mock private ProductPersistanceMapper productMapper;

    @InjectMocks
    private ProductRepositoryAdapter adapter;

    private ProductEntity entity;
    private Product domain;

    @BeforeEach
    void setUp() {
        entity = new ProductEntity();
        entity.setId(1L);
        entity.setName("T-Shirt");
        entity.setDescription("A cotton shirt");
        entity.setImageUrl("http://img.url/shirt.jpg");
        entity.setPrice(19.99);
        entity.setActive(true);

        domain = Product.builder()
                .id(1L).name("T-Shirt").description("A cotton shirt")
                .price(19.99).active(true).build();
    }

    @Nested
    @DisplayName("findById()")
    class FindById {

        @Test
        void shouldReturnMappedDomain_whenFound() {
            when(productJpaRepository.findById(1L)).thenReturn(Optional.of(entity));
            when(productMapper.toDomain(entity)).thenReturn(domain);

            Optional<Product> result = adapter.findById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo("T-Shirt");
        }

        @Test
        void shouldReturnEmpty_whenNotFound() {
            when(productJpaRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<Product> result = adapter.findById(99L);

            assertThat(result).isEmpty();
            verifyNoInteractions(productMapper);
        }
    }

    @Nested
    @DisplayName("findAll(ProductSearchCriteria)")
    class FindAll {

        @Test
        void shouldReturnPagedResult_withMappedContent() {
            ProductSearchCriteria criteria = new ProductSearchCriteria(
                    null, ProductSortField.ID, SortDirection.ASC, null, List.of(), 0, 10);
            PageImpl<ProductEntity> page = new PageImpl<>(List.of(entity));

            when(productJpaRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
            when(productMapper.toDomain(entity)).thenReturn(domain);

            PagedResult<Product> result = adapter.findAll(criteria);

            assertThat(result.content()).hasSize(1);
            assertThat(result.content().get(0).getName()).isEqualTo("T-Shirt");
            assertThat(result.page()).isEqualTo(0);
            assertThat(result.size()).isEqualTo(1);
            assertThat(result.hasNext()).isFalse();
        }

        @Test
        void shouldReturnEmptyPagedResult_whenNoProductsFound() {
            ProductSearchCriteria criteria = new ProductSearchCriteria(
                    "nonexistent", ProductSortField.PRICE, SortDirection.DESC, 5, List.of(), 0, 10);

            when(productJpaRepository.findAll(any(Specification.class), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of()));

            PagedResult<Product> result = adapter.findAll(criteria);

            assertThat(result.content()).isEmpty();
            assertThat(result.hasNext()).isFalse();
        }

        @Test
        void shouldPassCorrectPageable_forPriceSortDescending() {
            ProductSearchCriteria criteria = new ProductSearchCriteria(
                    null, ProductSortField.PRICE, SortDirection.DESC, null, List.of(), 2, 5);

            when(productJpaRepository.findAll(any(Specification.class), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of()));

            adapter.findAll(criteria);

            verify(productJpaRepository).findAll(any(Specification.class), any(Pageable.class));
        }
    }

    @Nested
    @DisplayName("findAllByIdIn()")
    class FindAllByIdIn {

        @Test
        void shouldReturnMappedList() {
            List<Long> ids = List.of(1L);
            when(productJpaRepository.findAllByIdIn(ids)).thenReturn(List.of(entity));
            when(productMapper.toDomainList(List.of(entity))).thenReturn(List.of(domain));

            List<Product> result = adapter.findAllByIdIn(ids);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("T-Shirt");
        }

        @Test
        void shouldReturnEmptyList_whenNoneFound() {
            List<Long> ids = List.of(99L);
            when(productJpaRepository.findAllByIdIn(ids)).thenReturn(List.of());
            when(productMapper.toDomainList(List.of())).thenReturn(List.of());

            assertThat(adapter.findAllByIdIn(ids)).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAllByIdInSet()")
    class FindAllByIdInSet {

        @Test
        void shouldReturnMappedSet() {
            Set<Long> ids = Set.of(1L);
            Set<ProductEntity> entitySet = Set.of(entity);
            Set<Product> domainSet = Set.of(domain);

            when(productJpaRepository.findAllByIdIn(ids)).thenReturn(entitySet);
            when(productMapper.toDomainSet(entitySet)).thenReturn(domainSet);

            Set<Product> result = adapter.findAllByIdInSet(ids);

            assertThat(result).hasSize(1);
        }

        @Test
        void shouldReturnEmptySet_whenNoneFound() {
            Set<Long> ids = Set.of(99L);
            when(productJpaRepository.findAllByIdIn(ids)).thenReturn(Set.of());
            when(productMapper.toDomainSet(Set.of())).thenReturn(Set.of());

            assertThat(adapter.findAllByIdInSet(ids)).isEmpty();
        }
    }
}
