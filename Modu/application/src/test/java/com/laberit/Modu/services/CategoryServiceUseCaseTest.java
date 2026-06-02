package com.laberit.Modu.services;

import com.laberit.Modu.domain.exceptions.CategoryNotFoundException;
import com.laberit.Modu.domain.model.Category;
import com.laberit.Modu.ports.driven.CategoryRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class CategoryServiceUseCaseTest {

    @Mock
    private CategoryRepositoryPort categoryRepositoryPort;

    @InjectMocks
    private CategoryServiceUseCase categoryServiceUseCase;

    @Nested
    @DisplayName("findAll() tests")
    class FindAlltests {
        @Test
        @DisplayName("If categories do not exist in repository, an empty list is returned")
        void findAllReturnsEmptyListWhenNoCategoriesExist() {
            // given
            Mockito.when(categoryRepositoryPort.findAll()).thenReturn(List.of());

            // when
            List<Category> result = categoryServiceUseCase.findAll();

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("If categories exist in repository, returns a List of categories")
        void findAllReturnsAListOfCategoriesWhenCategoriesExist() {
            // given
            Category category1 = Category.builder().id(1).name("Activewear").build();
            Category category2 = Category.builder().id(2).name("Streetwear").build();
            List<Category> categories = List.of(category1, category2);
            Mockito.when(categoryRepositoryPort.findAll()).thenReturn(categories);

            // when
            List<Category> result = categoryServiceUseCase.findAll();

            // then
            assertThat(result)
                    .hasSize(2)
                    .containsExactly(category1, category2);
        }
    }

    @Nested
    @DisplayName("findCategoryById tests")
    class FindCategoryByIdTests {

        @Test
        @DisplayName("When the id exists, a category with the indicated id is returned")
        void findCategoryByIdWhenIdExistReturnTheRequiredCategory() {
            // given
            Category categoryExpected = Category.builder().id(1).name("Activewear").build();
            Integer id = 1;
            Mockito.when(categoryRepositoryPort.findById(id)).thenReturn(Optional.of(categoryExpected));

            // when
            Category result = categoryServiceUseCase.findCategoryById(id);

            // then
            assertThat(result)
                    .isEqualTo(categoryExpected);
        }

        @Test
        @DisplayName("When the id does not exists, a CategoryNotFoundException is thrown")
        void findCategoryByIdWhenIdDoesNotExistCategoryNotFoundExceptionIsThrown() {
            // given
            Integer id = 100;
            Mockito.when(categoryRepositoryPort.findById(id)).thenReturn(Optional.empty());

            // when-then
            assertThatThrownBy(() -> categoryServiceUseCase.findCategoryById(id))
                    .isInstanceOf(CategoryNotFoundException.class)
                    .hasMessageContaining(id.toString());
        }
    }
}