package com.laberit.Modu.repositories.mappers;

import com.laberit.Modu.domain.model.ProductCategory;
import com.laberit.Modu.repositories.models.ProductCategoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductCategoryPersistanceMapperTest {

    private ProductCategoryPersistanceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductCategoryPersistanceMapperImpl();
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntity {

        @Test
        void shouldReturnNull_whenProductCategoryIsNull() {
            assertThat(mapper.toEntity(null)).isNull();
        }

        @Test
        void shouldMapAllFields() {
            ProductCategory domain = ProductCategory.builder().id(1L).categoryId(10).productId(100L).build();

            ProductCategoryEntity entity = mapper.toEntity(domain);

            assertThat(entity.getId()).isEqualTo(1L);
            assertThat(entity.getCategoryId()).isEqualTo(10);
            assertThat(entity.getProductId()).isEqualTo(100L);
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
            ProductCategoryEntity entity = new ProductCategoryEntity();
            entity.setId(1L);
            entity.setCategoryId(10);
            entity.setProductId(100L);

            ProductCategory domain = mapper.toDomain(entity);

            assertThat(domain.id()).isEqualTo(1L);
            assertThat(domain.categoryId()).isEqualTo(10);
            assertThat(domain.productId()).isEqualTo(100L);
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
            ProductCategoryEntity e1 = new ProductCategoryEntity();
            e1.setId(1L);
            e1.setCategoryId(10);
            e1.setProductId(100L);
            ProductCategoryEntity e2 = new ProductCategoryEntity();
            e2.setId(2L);
            e2.setCategoryId(20);
            e2.setProductId(200L);

            List<ProductCategory> result = mapper.toDomainList(List.of(e1, e2));

            assertThat(result).hasSize(2);
            assertThat(result.get(0).productId()).isEqualTo(100L);
            assertThat(result.get(1).categoryId()).isEqualTo(20);
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
        void shouldMapEachProductCategory() {
            ProductCategory pc1 = ProductCategory.builder().id(1L).categoryId(10).productId(100L).build();
            ProductCategory pc2 = ProductCategory.builder().id(2L).categoryId(20).productId(200L).build();

            List<ProductCategoryEntity> result = mapper.toEntityList(List.of(pc1, pc2));

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getProductId()).isEqualTo(100L);
            assertThat(result.get(1).getCategoryId()).isEqualTo(20);
        }
    }
}
