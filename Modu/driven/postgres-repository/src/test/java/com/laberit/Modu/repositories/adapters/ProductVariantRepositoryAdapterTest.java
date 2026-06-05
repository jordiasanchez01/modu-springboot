package com.laberit.Modu.repositories.adapters;

import com.laberit.Modu.domain.model.ProductVariant;
import com.laberit.Modu.repositories.ProductVariantJpaRepository;
import com.laberit.Modu.repositories.mappers.ProductVariantPersistanceMapper;
import com.laberit.Modu.repositories.models.ProductVariantEntity;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductVariantRepositoryAdapterTest {

    @Mock private ProductVariantJpaRepository repository;
    @Mock private ProductVariantPersistanceMapper mapper;

    @InjectMocks
    private ProductVariantRepositoryAdapter adapter;

    private ProductVariantEntity entity;
    private ProductVariant domain;

    @BeforeEach
    void setUp() {
        entity = new ProductVariantEntity();
        entity.setId(1L);
        entity.setName("Red M");
        entity.setSize("M");
        entity.setColor("Red");
        entity.setStock(10);
        entity.setActive(true);

        domain = ProductVariant.builder()
                .id(1L).name("Red M").size("M").color("Red")
                .stock(10).active(true).productId(5L).build();
    }

    @Nested
    @DisplayName("findAllByProductId()")
    class FindAllByProductId {

        @Test
        void shouldReturnMappedList() {
            when(repository.findAllByProductId(5L)).thenReturn(List.of(entity));
            when(mapper.toDomainList(List.of(entity))).thenReturn(List.of(domain));

            List<ProductVariant> result = adapter.findAllByProductId(5L);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(1L);
        }

        @Test
        void shouldReturnEmptyList_whenNoneFound() {
            when(repository.findAllByProductId(99L)).thenReturn(List.of());
            when(mapper.toDomainList(List.of())).thenReturn(List.of());

            assertThat(adapter.findAllByProductId(99L)).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAllByIdIn()")
    class FindAllByIdIn {

        @Test
        void shouldReturnMappedList() {
            List<Long> ids = List.of(1L);
            when(repository.findAllById(ids)).thenReturn(List.of(entity));
            when(mapper.toDomainList(List.of(entity))).thenReturn(List.of(domain));

            List<ProductVariant> result = adapter.findAllByIdIn(ids);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("Red M");
        }
    }

    @Nested
    @DisplayName("findAllByIdInSet()")
    class FindAllByIdInSet {

        @Test
        void shouldReturnMappedSet() {
            Set<Long> ids = Set.of(1L);
            Set<ProductVariantEntity> entitySet = Set.of(entity);
            Set<ProductVariant> domainSet = Set.of(domain);

            when(repository.findAllByIdIn(ids)).thenReturn(entitySet);
            when(mapper.toDomainSet(entitySet)).thenReturn(domainSet);

            Set<ProductVariant> result = adapter.findAllByIdInSet(ids);

            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("findById()")
    class FindById {

        @Test
        void shouldReturnMappedDomain_whenFound() {
            when(repository.findById(1L)).thenReturn(Optional.of(entity));
            when(mapper.toDomain(entity)).thenReturn(domain);

            Optional<ProductVariant> result = adapter.findById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo("Red M");
        }

        @Test
        void shouldReturnEmpty_whenNotFound() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            Optional<ProductVariant> result = adapter.findById(99L);

            assertThat(result).isEmpty();
            verifyNoInteractions(mapper);
        }
    }

    @Nested
    @DisplayName("findByName()")
    class FindByName {

        @Test
        void shouldReturnMappedDomain_whenFound() {
            when(repository.findByName("Red M")).thenReturn(Optional.of(entity));
            when(mapper.toDomain(entity)).thenReturn(domain);

            assertThat(adapter.findByName("Red M")).isPresent();
        }

        @Test
        void shouldReturnEmpty_whenNotFound() {
            when(repository.findByName("Unknown")).thenReturn(Optional.empty());

            assertThat(adapter.findByName("Unknown")).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByProductId()")
    class FindByProductId {

        @Test
        void shouldReturnMappedDomain_whenFound() {
            when(repository.findByProductId(5L)).thenReturn(Optional.of(entity));
            when(mapper.toDomain(entity)).thenReturn(domain);

            assertThat(adapter.findByProductId(5L)).isPresent();
        }

        @Test
        void shouldReturnEmpty_whenNotFound() {
            when(repository.findByProductId(99L)).thenReturn(Optional.empty());

            assertThat(adapter.findByProductId(99L)).isEmpty();
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
    @DisplayName("existsByName()")
    class ExistsByName {

        @Test
        void shouldReturnTrue_whenExists() {
            when(repository.existsByName("Red M")).thenReturn(true);
            assertThat(adapter.existsByName("Red M")).isTrue();
        }

        @Test
        void shouldReturnFalse_whenNotExists() {
            when(repository.existsByName("Unknown")).thenReturn(false);
            assertThat(adapter.existsByName("Unknown")).isFalse();
        }
    }

    @Nested
    @DisplayName("save()")
    class Save {

        @Test
        void shouldConvertToEntity_persist_andConvertBack() {
            when(mapper.toEntity(domain)).thenReturn(entity);
            when(repository.save(entity)).thenReturn(entity);
            when(mapper.toDomain(entity)).thenReturn(domain);

            ProductVariant result = adapter.save(domain);

            assertThat(result.getName()).isEqualTo("Red M");
            verify(repository).save(entity);
        }
    }

    @Nested
    @DisplayName("deleteById()")
    class DeleteById {

        @Test
        void shouldDelegateToRepository() {
            adapter.deleteById(1L);
            verify(repository).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("saveAll()")
    class SaveAll {

        @Test
        void shouldConvertAll_persistAll_andConvertBack() {
            List<ProductVariant> domainList = List.of(domain);
            List<ProductVariantEntity> entityList = List.of(entity);

            when(mapper.toEntityList(domainList)).thenReturn(entityList);
            when(repository.saveAll(entityList)).thenReturn(entityList);
            when(mapper.toDomainList(entityList)).thenReturn(domainList);

            List<ProductVariant> result = adapter.saveAll(domainList);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(1L);
        }
    }
}
