package com.laberit.Modu.repositories.mappers;

import com.laberit.Modu.domain.model.Category;
import com.laberit.Modu.repositories.models.CategoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryPersistanceMapperTest {

    private CategoryPersistanceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CategoryPersistanceMapperImpl();
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntity {

        @Test
        void shouldReturnNull_whenCategoryIsNull() {
            assertThat(mapper.toEntity(null)).isNull();
        }

        @Test
        void shouldMapAllFields() {
            Category category = Category.builder().id(1).name("Streetwear").build();

            CategoryEntity entity = mapper.toEntity(category);

            assertThat(entity.getId()).isEqualTo(1);
            assertThat(entity.getName()).isEqualTo("Streetwear");
        }
    }

    @Nested
    @DisplayName("toDomain")
    class ToDomain {

        @Test
        void shouldReturnNull_whenEntityIsNull() {
            assertThat(mapper.toDomain(null)).isNull();
        }

        @Test
        void shouldMapAllFields() {
            CategoryEntity entity = new CategoryEntity();
            entity.setId(2);
            entity.setName("Activewear");

            Category category = mapper.toDomain(entity);

            assertThat(category.id()).isEqualTo(2);
            assertThat(category.name()).isEqualTo("Activewear");
        }
    }

    @Nested
    @DisplayName("toDomainList")
    class ToDomainList {

        @Test
        void shouldReturnNull_whenListIsNull() {
            assertThat(mapper.toDomainList(null)).isNull();
        }

        @Test
        void shouldMapEachEntity() {
            CategoryEntity e1 = new CategoryEntity();
            e1.setId(1);
            e1.setName("Streetwear");
            CategoryEntity e2 = new CategoryEntity();
            e2.setId(2);
            e2.setName("Activewear");

            List<Category> result = mapper.toDomainList(List.of(e1, e2));

            assertThat(result).hasSize(2);
            assertThat(result.get(0).id()).isEqualTo(1);
            assertThat(result.get(1).name()).isEqualTo("Activewear");
        }
    }

    @Nested
    @DisplayName("toEntityList")
    class ToEntityList {

        @Test
        void shouldReturnNull_whenListIsNull() {
            assertThat(mapper.toEntityList(null)).isNull();
        }

        @Test
        void shouldMapEachCategory() {
            Category c1 = Category.builder().id(1).name("Streetwear").build();
            Category c2 = Category.builder().id(2).name("Footwear").build();

            List<CategoryEntity> result = mapper.toEntityList(List.of(c1, c2));

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getId()).isEqualTo(1);
            assertThat(result.get(1).getName()).isEqualTo("Footwear");
        }
    }
}
