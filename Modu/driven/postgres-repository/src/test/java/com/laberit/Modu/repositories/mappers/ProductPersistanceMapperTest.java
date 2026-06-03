package com.laberit.Modu.repositories.mappers;

import com.laberit.Modu.domain.model.Product;
import com.laberit.Modu.repositories.models.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ProductPersistanceMapperTest {

    private ProductPersistanceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductPersistanceMapperImpl();
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntity {

        @Test
        void shouldReturnNull_whenProductIsNull() {
            assertThat(mapper.toEntity(null)).isNull();
        }

        @Test
        void shouldMapScalarFields() {
            Product product = Product.builder()
                    .id(1L).name("Classic Denim Jacket").description("A blue jacket")
                    .imageUrl("http://img.url").price(79.99).active(true)
                    .build();

            ProductEntity entity = mapper.toEntity(product);

            assertThat(entity.getId()).isEqualTo(1L);
            assertThat(entity.getName()).isEqualTo("Classic Denim Jacket");
            assertThat(entity.getDescription()).isEqualTo("A blue jacket");
            assertThat(entity.getImageUrl()).isEqualTo("http://img.url");
            assertThat(entity.getPrice()).isEqualTo(79.99);
            assertThat(entity.getActive()).isTrue();
        }

        @Test
        void shouldNotMapCategoriesOrVariants() {
            // categoriesSet and productVariantsList are intentionally excluded —
            // those relationships are managed by JPA and loaded separately.
            Product product = Product.builder().id(1L).name("T-Shirt").price(19.99).active(true).build();

            ProductEntity entity = mapper.toEntity(product);

            assertThat(entity.getCategoriesSet()).isNull();
            assertThat(entity.getProductVariantsSet()).isNull();
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
        void shouldMapScalarFields() {
            ProductEntity entity = new ProductEntity();
            entity.setId(1L);
            entity.setName("Classic Denim Jacket");
            entity.setDescription("A blue jacket");
            entity.setImageUrl("http://img.url");
            entity.setPrice(79.99);
            entity.setActive(true);

            Product product = mapper.toDomain(entity);

            assertThat(product.getId()).isEqualTo(1L);
            assertThat(product.getName()).isEqualTo("Classic Denim Jacket");
            assertThat(product.getDescription()).isEqualTo("A blue jacket");
            assertThat(product.getImageUrl()).isEqualTo("http://img.url");
            assertThat(product.getPrice()).isEqualTo(79.99);
            assertThat(product.getActive()).isTrue();
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
            ProductEntity e1 = new ProductEntity();
            e1.setId(1L);
            e1.setName("T-Shirt");
            e1.setPrice(19.99);
            e1.setActive(true);
            ProductEntity e2 = new ProductEntity();
            e2.setId(2L);
            e2.setName("Jeans");
            e2.setPrice(49.99);
            e2.setActive(true);

            List<Product> result = mapper.toDomainList(List.of(e1, e2));

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getId()).isEqualTo(1L);
            assertThat(result.get(1).getName()).isEqualTo("Jeans");
        }
    }

    @Nested
    @DisplayName("toDomainSet")
    class ToDomainSet {

        @Test
        void shouldReturnNull_whenSetIsNull() {
            assertThat(mapper.toDomainSet(null)).isNull();
        }

        @Test
        void shouldMapEachEntityInSet() {
            ProductEntity e1 = new ProductEntity();
            e1.setId(1L);
            e1.setName("T-Shirt");
            e1.setPrice(19.99);
            e1.setActive(true);

            Set<Product> result = mapper.toDomainSet(Set.of(e1));

            assertThat(result).hasSize(1);
            assertThat(result.iterator().next().getId()).isEqualTo(1L);
        }
    }
}
