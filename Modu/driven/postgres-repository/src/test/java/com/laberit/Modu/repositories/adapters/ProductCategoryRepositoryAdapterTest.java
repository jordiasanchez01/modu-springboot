package com.laberit.Modu.repositories.adapters;

import com.laberit.Modu.domain.model.ProductCategory;
import com.laberit.Modu.repositories.ProductCategoryJpaRepository;
import com.laberit.Modu.repositories.mappers.ProductCategoryPersistanceMapper;
import com.laberit.Modu.repositories.models.ProductCategoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductCategoryRepositoryAdapterTest {

    @Mock private ProductCategoryJpaRepository repository;
    @Mock private ProductCategoryPersistanceMapper mapper;

    @InjectMocks
    private ProductCategoryRepositoryAdapter adapter;

    private ProductCategoryEntity entity;
    private ProductCategory domain;

    @BeforeEach
    void setUp() {
        entity = new ProductCategoryEntity();
        entity.setId(1L);
        entity.setCategoryId(10);
        entity.setProductId(100L);

        domain = ProductCategory.builder().id(1L).categoryId(10).productId(100L).build();
    }

    @Nested
    @DisplayName("findById()")
    class FindById {

        @Test
        void shouldReturnMappedDomain_whenFound() {
            when(repository.findById(1L)).thenReturn(Optional.of(entity));
            when(mapper.toDomain(entity)).thenReturn(domain);

            Optional<ProductCategory> result = adapter.findById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().id()).isEqualTo(1L);
            assertThat(result.get().categoryId()).isEqualTo(10);
        }

        @Test
        void shouldReturnEmpty_whenNotFound() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            Optional<ProductCategory> result = adapter.findById(99L);

            assertThat(result).isEmpty();
            verifyNoInteractions(mapper);
        }
    }

    @Nested
    @DisplayName("findAllByCategoryId()")
    class FindAllByCategoryId {

        @Test
        void shouldReturnMappedList() {
            when(repository.findAllByCategoryId(10)).thenReturn(List.of(entity));
            when(mapper.toDomainList(List.of(entity))).thenReturn(List.of(domain));

            List<ProductCategory> result = adapter.findAllByCategoryId(10);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).categoryId()).isEqualTo(10);
        }

        @Test
        void shouldReturnEmptyList_whenNoneFound() {
            when(repository.findAllByCategoryId(99)).thenReturn(List.of());
            when(mapper.toDomainList(List.of())).thenReturn(List.of());

            assertThat(adapter.findAllByCategoryId(99)).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAllByProductId()")
    class FindAllByProductId {

        @Test
        void shouldReturnMappedList() {
            when(repository.findAllByProductId(100L)).thenReturn(List.of(entity));
            when(mapper.toDomainList(List.of(entity))).thenReturn(List.of(domain));

            List<ProductCategory> result = adapter.findAllByProductId(100L);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).productId()).isEqualTo(100L);
        }

        @Test
        void shouldReturnEmptyList_whenNoneFound() {
            when(repository.findAllByProductId(999L)).thenReturn(List.of());
            when(mapper.toDomainList(List.of())).thenReturn(List.of());

            assertThat(adapter.findAllByProductId(999L)).isEmpty();
        }
    }

    @Nested
    @DisplayName("existsById()")
    class ExistsById {

        @Test
        void shouldReturnTrue_whenExists() {
            when(repository.existsById(1L)).thenReturn(true);
            assertThat(adapter.existsById(1L)).isTrue();
        }

        @Test
        void shouldReturnFalse_whenNotExists() {
            when(repository.existsById(99L)).thenReturn(false);
            assertThat(adapter.existsById(99L)).isFalse();
        }
    }

    @Nested
    @DisplayName("existsByCategoryId()")
    class ExistsByCategoryId {

        @Test
        void shouldReturnTrue_whenExists() {
            when(repository.existsByCategoryId(10)).thenReturn(true);
            assertThat(adapter.existsByCategoryId(10)).isTrue();
        }

        @Test
        void shouldReturnFalse_whenNotExists() {
            when(repository.existsByCategoryId(99)).thenReturn(false);
            assertThat(adapter.existsByCategoryId(99)).isFalse();
        }
    }

    @Nested
    @DisplayName("existsByProductId()")
    class ExistsByProductId {

        @Test
        void shouldReturnTrue_whenExists() {
            when(repository.existsByProductId(100L)).thenReturn(true);
            assertThat(adapter.existsByProductId(100L)).isTrue();
        }

        @Test
        void shouldReturnFalse_whenNotExists() {
            when(repository.existsByProductId(999L)).thenReturn(false);
            assertThat(adapter.existsByProductId(999L)).isFalse();
        }
    }
}
