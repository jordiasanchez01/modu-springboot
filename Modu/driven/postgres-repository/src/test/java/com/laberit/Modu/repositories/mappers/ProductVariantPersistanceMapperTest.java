package com.laberit.Modu.repositories.mappers;

import com.laberit.Modu.domain.model.ProductVariant;
import com.laberit.Modu.repositories.models.ProductEntity;
import com.laberit.Modu.repositories.models.ProductVariantEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ProductVariantPersistanceMapperTest {

    private ProductVariantPersistanceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductVariantPersistanceMapperImpl();
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntity {

        @Test
        void shouldReturnNull_whenProductVariantIsNull() {
            assertThat(mapper.toEntity(null)).isNull();
        }

        @Test
        void shouldMapAllScalarFields() {
            ProductVariant domain = ProductVariant.builder()
                    .id(1L).name("1_S_BLUE").size("S").color("BLUE")
                    .stock(7).active(true).productId(5L)
                    .build();

            ProductVariantEntity entity = mapper.toEntity(domain);

            assertThat(entity.getId()).isEqualTo(1L);
            assertThat(entity.getName()).isEqualTo("1_S_BLUE");
            assertThat(entity.getSize()).isEqualTo("S");
            assertThat(entity.getColor()).isEqualTo("BLUE");
            assertThat(entity.getStock()).isEqualTo(7);
            assertThat(entity.getActive()).isTrue();
        }

        @Test
        void shouldCreateProductReference_withOnlyProductId() {
            // toEntity creates a shell ProductEntity containing just the FK —
            // it does not load the full product graph.
            ProductVariant domain = ProductVariant.builder()
                    .id(1L).name("1_S_BLUE").size("S").color("BLUE")
                    .stock(7).active(true).productId(5L)
                    .build();

            ProductVariantEntity entity = mapper.toEntity(domain);

            assertThat(entity.getProduct()).isNotNull();
            assertThat(entity.getProduct().getId()).isEqualTo(5L);
            assertThat(entity.getProduct().getName()).isNull();
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
        void shouldMapAllFields_andExtractProductIdFromProductReference() {
            ProductEntity productRef = new ProductEntity();
            productRef.setId(5L);

            ProductVariantEntity entity = new ProductVariantEntity();
            entity.setId(1L);
            entity.setName("1_S_BLUE");
            entity.setSize("S");
            entity.setColor("BLUE");
            entity.setStock(7);
            entity.setActive(true);
            entity.setProduct(productRef);

            ProductVariant domain = mapper.toDomain(entity);

            assertThat(domain.getId()).isEqualTo(1L);
            assertThat(domain.getName()).isEqualTo("1_S_BLUE");
            assertThat(domain.getSize()).isEqualTo("S");
            assertThat(domain.getColor()).isEqualTo("BLUE");
            assertThat(domain.getStock()).isEqualTo(7);
            assertThat(domain.getActive()).isTrue();
            assertThat(domain.getProductId()).isEqualTo(5L);
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
            ProductEntity productRef = new ProductEntity();
            productRef.setId(5L);

            ProductVariantEntity e1 = new ProductVariantEntity();
            e1.setId(1L);
            e1.setName("1_S_BLUE");
            e1.setSize("S");
            e1.setColor("BLUE");
            e1.setStock(7);
            e1.setActive(true);
            e1.setProduct(productRef);

            ProductVariantEntity e2 = new ProductVariantEntity();
            e2.setId(2L);
            e2.setName("1_S_BLACK");
            e2.setSize("S");
            e2.setColor("BLACK");
            e2.setStock(3);
            e2.setActive(true);
            e2.setProduct(productRef);

            List<ProductVariant> result = mapper.toDomainList(List.of(e1, e2));

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getId()).isEqualTo(1L);
            assertThat(result.get(0).getProductId()).isEqualTo(5L);
            assertThat(result.get(1).getName()).isEqualTo("1_S_BLACK");
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
            ProductEntity productRef = new ProductEntity();
            productRef.setId(5L);

            ProductVariantEntity entity = new ProductVariantEntity();
            entity.setId(1L);
            entity.setName("1_S_BLUE");
            entity.setSize("S");
            entity.setColor("BLUE");
            entity.setStock(7);
            entity.setActive(true);
            entity.setProduct(productRef);

            Set<ProductVariant> result = mapper.toDomainSet(Set.of(entity));

            assertThat(result).hasSize(1);
            assertThat(result.iterator().next().getProductId()).isEqualTo(5L);
        }
    }
}
