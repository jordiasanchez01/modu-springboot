package com.laberit.Modu.repositories.adapters;

import com.laberit.Modu.domain.model.Category;
import com.laberit.Modu.repositories.CategoryJpaRepository;
import com.laberit.Modu.repositories.mappers.CategoryPersistanceMapper;
import com.laberit.Modu.repositories.models.CategoryEntity;
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
class CategoryRepositoryAdapterTest {

    @Mock private CategoryJpaRepository repository;
    @Mock private CategoryPersistanceMapper mapper;

    @InjectMocks
    private CategoryRepositoryAdapter adapter;

    private CategoryEntity entity;
    private Category domain;

    @BeforeEach
    void setUp() {
        entity = new CategoryEntity();
        entity.setId(1);
        entity.setName("Electronics");

        domain = Category.builder().id(1).name("Electronics").build();
    }

    @Nested
    @DisplayName("findAll()")
    class FindAll {

        @Test
        void shouldReturnMappedDomainList() {
            when(repository.findAll()).thenReturn(List.of(entity));
            when(mapper.toDomainList(List.of(entity))).thenReturn(List.of(domain));

            List<Category> result = adapter.findAll();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).name()).isEqualTo("Electronics");
        }

        @Test
        void shouldReturnEmptyList_whenNoCategories() {
            when(repository.findAll()).thenReturn(List.of());
            when(mapper.toDomainList(List.of())).thenReturn(List.of());

            assertThat(adapter.findAll()).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAllByIdIn()")
    class FindAllByIdIn {

        @Test
        void shouldReturnMappedDomainSet() {
            Set<Integer> ids = Set.of(1);
            when(repository.findAllById(ids)).thenReturn(List.of(entity));
            when(mapper.toDomainList(List.of(entity))).thenReturn(List.of(domain));

            Set<Category> result = adapter.findAllByIdIn(ids);

            assertThat(result).hasSize(1);
            assertThat(result.iterator().next().id()).isEqualTo(1);
        }

        @Test
        void shouldReturnEmptySet_whenNoneFound() {
            Set<Integer> ids = Set.of(99);
            when(repository.findAllById(ids)).thenReturn(List.of());
            when(mapper.toDomainList(List.of())).thenReturn(List.of());

            assertThat(adapter.findAllByIdIn(ids)).isEmpty();
        }
    }

    @Nested
    @DisplayName("findById()")
    class FindById {

        @Test
        void shouldReturnMappedDomain_whenFound() {
            when(repository.findById(1)).thenReturn(Optional.of(entity));
            when(mapper.toDomain(entity)).thenReturn(domain);

            Optional<Category> result = adapter.findById(1);

            assertThat(result).isPresent();
            assertThat(result.get().name()).isEqualTo("Electronics");
        }

        @Test
        void shouldReturnEmpty_whenNotFound() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            Optional<Category> result = adapter.findById(99);

            assertThat(result).isEmpty();
            verifyNoInteractions(mapper);
        }
    }

    @Nested
    @DisplayName("findByName()")
    class FindByName {

        @Test
        void shouldReturnMappedDomain_whenFound() {
            when(repository.findByName("Electronics")).thenReturn(Optional.of(entity));
            when(mapper.toDomain(entity)).thenReturn(domain);

            Optional<Category> result = adapter.findByName("Electronics");

            assertThat(result).isPresent();
            assertThat(result.get().name()).isEqualTo("Electronics");
        }

        @Test
        void shouldReturnEmpty_whenNotFound() {
            when(repository.findByName("Unknown")).thenReturn(Optional.empty());

            Optional<Category> result = adapter.findByName("Unknown");

            assertThat(result).isEmpty();
            verifyNoInteractions(mapper);
        }
    }

    @Nested
    @DisplayName("existsById()")
    class ExistsById {

        @Test
        void shouldReturnTrue_whenExists() {
            when(repository.existsById(1)).thenReturn(true);
            assertThat(adapter.existsById(1)).isTrue();
        }

        @Test
        void shouldReturnFalse_whenNotExists() {
            when(repository.existsById(99)).thenReturn(false);
            assertThat(adapter.existsById(99)).isFalse();
        }
    }

    @Nested
    @DisplayName("existsByName()")
    class ExistsByName {

        @Test
        void shouldReturnTrue_whenExists() {
            when(repository.existsByName("Electronics")).thenReturn(true);
            assertThat(adapter.existsByName("Electronics")).isTrue();
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

            Category result = adapter.save(domain);

            assertThat(result.name()).isEqualTo("Electronics");
            verify(repository).save(entity);
        }
    }

    @Nested
    @DisplayName("deleteById()")
    class DeleteById {

        @Test
        void shouldDelegateToRepository() {
            adapter.deleteById(1);
            verify(repository).deleteById(1);
        }
    }
}
