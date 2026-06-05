package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.Category;
import com.laberit.Modu.rest.generated.model.HomeCategoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryRestMapperTest {

    private CategoryRestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CategoryRestMapperImpl();
    }

    @Nested
    @DisplayName("toHomeCategoryResponse")
    class ToHomeCategoryResponse {

        @Test
        void shouldReturnNull_whenCategoryIsNull() {
            assertThat(mapper.toHomeCategoryResponse(null)).isNull();
        }

        @Test
        void shouldMapName() {
            Category category = Category.builder().id(1).name("Streetwear").build();

            HomeCategoryResponse result = mapper.toHomeCategoryResponse(category);

            assertThat(result.getName()).isEqualTo("Streetwear");
        }
    }

    @Nested
    @DisplayName("toHomeCategoryResponseList")
    class ToHomeCategoryResponseList {

        @Test
        void shouldReturnNull_whenListIsNull() {
            assertThat(mapper.toHomeCategoryResponseList(null)).isNull();
        }

        @Test
        void shouldMapEachCategory() {
            List<Category> categories = List.of(
                    Category.builder().id(1).name("Streetwear").build(),
                    Category.builder().id(2).name("Activewear").build()
            );

            List<HomeCategoryResponse> result = mapper.toHomeCategoryResponseList(categories);

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getName()).isEqualTo("Streetwear");
            assertThat(result.get(1).getName()).isEqualTo("Activewear");
        }
    }
}
