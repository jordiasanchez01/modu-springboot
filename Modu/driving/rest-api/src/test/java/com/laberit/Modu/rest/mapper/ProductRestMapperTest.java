package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.Category;
import com.laberit.Modu.domain.model.Product;
import com.laberit.Modu.domain.model.ProductVariant;
import com.laberit.Modu.domain.model.response.PagedResult;
import com.laberit.Modu.rest.generated.model.ProductDetailsResponse;
import com.laberit.Modu.rest.generated.model.ProductPageResponse;
import com.laberit.Modu.rest.generated.model.ProductsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ProductRestMapperTest {

    private ProductRestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductRestMapperImpl();
    }

    private Product sampleProduct() {
        return Product.builder()
                .id(1L).name("Classic Denim Jacket").description("A blue jacket")
                .imageUrl("http://img.url/jacket.jpg").price(79.99).active(true)
                .categoriesSet(Set.of(
                        Category.builder().id(1).name("Streetwear").build(),
                        Category.builder().id(6).name("Vintage").build()
                ))
                .productVariantsList(List.of(
                        ProductVariant.builder().id(1L).name("1_S_BLUE").size("S").color("BLUE")
                                .stock(7).active(true).productId(1L).build()
                ))
                .build();
    }

    @Nested
    @DisplayName("toProductsResponse")
    class ToProductsResponse {

        @Test
        void shouldReturnNull_whenProductIsNull() {
            assertThat(mapper.toProductsResponse(null)).isNull();
        }

        @Test
        void shouldMapIdToProductId_andImageUrlToUrl() {
            ProductsResponse result = mapper.toProductsResponse(sampleProduct());

            assertThat(result.getProductId()).isEqualTo(1L);
            assertThat(result.getUrl()).isEqualTo("http://img.url/jacket.jpg");
        }
    }

    @Nested
    @DisplayName("toProductDetailsResponse")
    class ToProductDetailsResponse {

        @Test
        void shouldReturnNull_whenProductIsNull() {
            assertThat(mapper.toProductDetailsResponse(null)).isNull();
        }

        @Test
        void shouldMapAllScalarFields() {
            ProductDetailsResponse result = mapper.toProductDetailsResponse(sampleProduct());

            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("Classic Denim Jacket");
            assertThat(result.getDescription()).isEqualTo("A blue jacket");
            assertThat(result.getImageUrl()).isEqualTo("http://img.url/jacket.jpg");
            assertThat(result.getPrice()).isEqualTo(79.99);
            assertThat(result.getActive()).isTrue();
        }

        @Test
        void shouldMapCategoriesSet() {
            ProductDetailsResponse result = mapper.toProductDetailsResponse(sampleProduct());

            assertThat(result.getCategoriesSet()).hasSize(2);
            assertThat(result.getCategoriesSet())
                    .extracting("name")
                    .containsExactlyInAnyOrder("Streetwear", "Vintage");
        }

        @Test
        void shouldMapProductVariantsList() {
            ProductDetailsResponse result = mapper.toProductDetailsResponse(sampleProduct());

            assertThat(result.getProductVariantsList()).hasSize(1);
            assertThat(result.getProductVariantsList().get(0).getName()).isEqualTo("1_S_BLUE");
            assertThat(result.getProductVariantsList().get(0).getSize()).isEqualTo("S");
            assertThat(result.getProductVariantsList().get(0).getColor()).isEqualTo("BLUE");
            assertThat(result.getProductVariantsList().get(0).getProductId()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("toProductPageResponse")
    class ToProductPageResponse {

        @Test
        void shouldMapDataList_andPaginationMeta() {
            Product p1 = Product.builder().id(1L).imageUrl("http://img1.url").build();
            Product p2 = Product.builder().id(2L).imageUrl("http://img2.url").build();
            PagedResult<Product> pagedResult = new PagedResult<>(List.of(p1, p2), 0, 2, true);

            ProductPageResponse result = mapper.toProductPageResponse(pagedResult);

            assertThat(result.getData()).hasSize(2);
            assertThat(result.getData().get(0).getProductId()).isEqualTo(1L);
            assertThat(result.getData().get(1).getProductId()).isEqualTo(2L);
        }

        @Test
        void shouldMapPaginationMeta() {
            PagedResult<Product> pagedResult = new PagedResult<>(List.of(), 3, 0, false);

            ProductPageResponse result = mapper.toProductPageResponse(pagedResult);

            assertThat(result.getMeta().getPage()).isEqualTo(3);
            assertThat(result.getMeta().getSize()).isEqualTo(0);
            assertThat(result.getMeta().getHasNext()).isFalse();
        }

        @Test
        void shouldReturnEmptyData_whenContentIsEmpty() {
            PagedResult<Product> pagedResult = new PagedResult<>(List.of(), 0, 0, false);

            ProductPageResponse result = mapper.toProductPageResponse(pagedResult);

            assertThat(result.getData()).isEmpty();
        }
    }
}
