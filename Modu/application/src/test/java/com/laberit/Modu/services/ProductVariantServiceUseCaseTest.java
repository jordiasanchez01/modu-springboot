package com.laberit.Modu.services;

import com.laberit.Modu.domain.exceptions.ProductVariantNotAvailableException;
import com.laberit.Modu.domain.model.ProductVariant;
import com.laberit.Modu.ports.driven.ProductVariantRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductVariantServiceUseCaseTest {

    @Mock
    private ProductVariantRepositoryPort productVariantRepositoryPort;

    @InjectMocks
    private ProductVariantServiceUseCase service;

    @Nested
    @DisplayName("assertIsValidToPurchase()")
    class AssertIsValidToPurchase {

        @Test
        void shouldNotThrow_whenVariantIsActiveAndHasSufficientStock() {
            ProductVariant variant = ProductVariant.builder().id(1L).active(true).stock(5).build();

            assertThatNoException().isThrownBy(() -> service.assertIsValidToPurchase(variant, 3));
        }

        @Test
        void shouldThrow_whenVariantIsInactive() {
            ProductVariant variant = ProductVariant.builder().id(1L).active(false).stock(5).build();

            assertThatThrownBy(() -> service.assertIsValidToPurchase(variant, 1))
                    .isInstanceOf(ProductVariantNotAvailableException.class);
        }

        @Test
        void shouldThrow_whenStockIsInsufficient() {
            ProductVariant variant = ProductVariant.builder().id(1L).active(true).stock(2).build();

            assertThatThrownBy(() -> service.assertIsValidToPurchase(variant, 5))
                    .isInstanceOf(ProductVariantNotAvailableException.class);
        }

        @Test
        void shouldThrow_whenVariantIsInactiveAndStockIsAlsoInsufficient() {
            ProductVariant variant = ProductVariant.builder().id(1L).active(false).stock(0).build();

            assertThatThrownBy(() -> service.assertIsValidToPurchase(variant, 1))
                    .isInstanceOf(ProductVariantNotAvailableException.class);
        }
    }

    @Nested
    @DisplayName("sortBySizeThenColor()")
    class SortBySizeThenColor {

        @Test
        void shouldSortNumericSizesNumerically() {
            List<ProductVariant> variants = List.of(
                    ProductVariant.builder().size("42").color("BLACK").build(),
                    ProductVariant.builder().size("35").color("BLACK").build(),
                    ProductVariant.builder().size("39").color("BLACK").build()
            );

            List<ProductVariant> result = service.sortBySizeThenColor(variants);

            assertThat(result).extracting(ProductVariant::getSize)
                    .containsExactly("35", "39", "42");
        }

        @Test
        void shouldSortLetteredSizesByStandardOrder() {
            List<ProductVariant> variants = List.of(
                    ProductVariant.builder().size("XL").color("BLACK").build(),
                    ProductVariant.builder().size("XXS").color("BLACK").build(),
                    ProductVariant.builder().size("S").color("BLACK").build(),
                    ProductVariant.builder().size("M").color("BLACK").build()
            );

            List<ProductVariant> result = service.sortBySizeThenColor(variants);

            assertThat(result).extracting(ProductVariant::getSize)
                    .containsExactly("XXS", "S", "M", "XL");
        }

        @Test
        void shouldPlaceNumericSizesBeforeLetteredSizes() {
            List<ProductVariant> variants = List.of(
                    ProductVariant.builder().size("M").color("BLACK").build(),
                    ProductVariant.builder().size("42").color("BLACK").build()
            );

            List<ProductVariant> result = service.sortBySizeThenColor(variants);

            assertThat(result).extracting(ProductVariant::getSize)
                    .containsExactly("42", "M");
        }

        @Test
        void shouldPlaceUnknownSizesLast() {
            List<ProductVariant> variants = List.of(
                    ProductVariant.builder().size("ONESIZE").color("BLACK").build(),
                    ProductVariant.builder().size("42").color("BLACK").build(),
                    ProductVariant.builder().size("M").color("BLACK").build()
            );

            List<ProductVariant> result = service.sortBySizeThenColor(variants);

            assertThat(result).extracting(ProductVariant::getSize)
                    .containsExactly("42", "M", "ONESIZE");
        }

        @Test
        void shouldSortByColorAlphabetically_withinSameSize() {
            List<ProductVariant> variants = List.of(
                    ProductVariant.builder().size("M").color("RED").build(),
                    ProductVariant.builder().size("M").color("BLUE").build(),
                    ProductVariant.builder().size("M").color("BLACK").build()
            );

            List<ProductVariant> result = service.sortBySizeThenColor(variants);

            assertThat(result).extracting(ProductVariant::getColor)
                    .containsExactly("BLACK", "BLUE", "RED");
        }

        @Test
        void shouldBeCaseInsensitiveForLetteredSizes() {
            List<ProductVariant> variants = List.of(
                    ProductVariant.builder().size("xl").color("BLACK").build(),
                    ProductVariant.builder().size("S").color("BLACK").build()
            );

            List<ProductVariant> result = service.sortBySizeThenColor(variants);

            // lowercase "xl" should be treated as XL (index 5), after S (index 2)
            assertThat(result).extracting(ProductVariant::getSize)
                    .containsExactly("S", "xl");
        }
    }

    @Nested
    @DisplayName("findAllByProductId()")
    class FindAllByProductId {

        @Test
        void shouldReturnVariantsSortedBySizeThenColor() {
            List<ProductVariant> unsorted = List.of(
                    ProductVariant.builder().size("XL").color("BLACK").build(),
                    ProductVariant.builder().size("S").color("RED").build(),
                    ProductVariant.builder().size("S").color("BLUE").build()
            );
            when(productVariantRepositoryPort.findAllByProductId(1L)).thenReturn(unsorted);

            List<ProductVariant> result = service.findAllByProductId(1L);

            assertThat(result).extracting(ProductVariant::getSize)
                    .containsExactly("S", "S", "XL");
            assertThat(result.get(0).getColor()).isEqualTo("BLUE");
            assertThat(result.get(1).getColor()).isEqualTo("RED");
        }
    }
}
