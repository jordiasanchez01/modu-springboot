package com.laberit.Modu.repositories.specifications;

import com.laberit.Modu.repositories.models.CategoryEntity;
import com.laberit.Modu.repositories.models.ProductEntity;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductSpecificationTest {

    @Mock private Root<ProductEntity> root;
    @Mock private CriteriaQuery<?> query;
    @Mock private CriteriaBuilder cb;
    @Mock private Predicate predicate;
    @Mock private Predicate conjunction;



    @Nested
    @DisplayName("hasTitle()")
    class HasTitle {

        @Test
        void shouldReturnConjunction_whenTitleIsNull() {
            when(cb.conjunction()).thenReturn(conjunction);

            Predicate result = ProductSpecification.hasTitle(null)
                    .toPredicate(root, query, cb);

            assertThat(result).isSameAs(conjunction);
            verify(cb).conjunction();
            verifyNoMoreInteractions(root);
        }

        @SuppressWarnings("unchecked")
        @Test
        void shouldCreateLikePredicate_wrappingTitleWithWildcards() {
            Path<String> namePath = mock(Path.class);
            Expression<String> lowerName = mock(Expression.class);
            doReturn(namePath).when(root).get("name");
            when(cb.lower(namePath)).thenReturn(lowerName);
            when(cb.like(eq(lowerName), any(String.class), eq('\\'))).thenReturn(predicate);

            ArgumentCaptor<String> patternCaptor = ArgumentCaptor.forClass(String.class);

            ProductSpecification.hasTitle("Denim").toPredicate(root, query, cb);

            verify(cb).like(eq(lowerName), patternCaptor.capture(), eq('\\'));
            assertThat(patternCaptor.getValue()).isEqualTo("%denim%");
        }

        @SuppressWarnings("unchecked")
        @Test
        void shouldEscapePercent_inTitlePattern() {
            Path<String> namePath = mock(Path.class);
            Expression<String> lowerName = mock(Expression.class);
            doReturn(namePath).when(root).get("name");
            when(cb.lower(namePath)).thenReturn(lowerName);
            when(cb.like(eq(lowerName), any(String.class), eq('\\'))).thenReturn(predicate);

            ArgumentCaptor<String> patternCaptor = ArgumentCaptor.forClass(String.class);

            ProductSpecification.hasTitle("50%off").toPredicate(root, query, cb);

            verify(cb).like(eq(lowerName), patternCaptor.capture(), eq('\\'));
            assertThat(patternCaptor.getValue()).isEqualTo("%50\\%off%");
        }

        @SuppressWarnings("unchecked")
        @Test
        void shouldEscapeUnderscore_inTitlePattern() {
            Path<String> namePath = mock(Path.class);
            Expression<String> lowerName = mock(Expression.class);
            doReturn(namePath).when(root).get("name");
            when(cb.lower(namePath)).thenReturn(lowerName);
            when(cb.like(eq(lowerName), any(String.class), eq('\\'))).thenReturn(predicate);

            ArgumentCaptor<String> patternCaptor = ArgumentCaptor.forClass(String.class);

            ProductSpecification.hasTitle("t_shirt").toPredicate(root, query, cb);

            verify(cb).like(eq(lowerName), patternCaptor.capture(), eq('\\'));
            assertThat(patternCaptor.getValue()).isEqualTo("%t\\_shirt%");
        }

        @SuppressWarnings("unchecked")
        @Test
        void shouldEscapeBackslash_inTitlePattern() {
            Path<String> namePath = mock(Path.class);
            Expression<String> lowerName = mock(Expression.class);
            doReturn(namePath).when(root).get("name");
            when(cb.lower(namePath)).thenReturn(lowerName);
            when(cb.like(eq(lowerName), any(String.class), eq('\\'))).thenReturn(predicate);

            ArgumentCaptor<String> patternCaptor = ArgumentCaptor.forClass(String.class);

            ProductSpecification.hasTitle("a\\b").toPredicate(root, query, cb);

            verify(cb).like(eq(lowerName), patternCaptor.capture(), eq('\\'));
            assertThat(patternCaptor.getValue()).isEqualTo("%a\\\\b%");
        }
    }

    @Nested
    @DisplayName("hasMaxPrice()")
    class HasMaxPrice {

        @Test
        void shouldReturnConjunction_whenMaxPriceIsNull() {
            when(cb.conjunction()).thenReturn(conjunction);

            Predicate result = ProductSpecification.hasMaxPrice(null)
                    .toPredicate(root, query, cb);

            assertThat(result).isSameAs(conjunction);
            verify(cb).conjunction();
        }

        @SuppressWarnings("unchecked")
        @Test
        void shouldCreateLessThanOrEqualToPredicate_whenMaxPriceProvided() {
            Path<Double> pricePath = mock(Path.class);
            doReturn(pricePath).when(root).get("price");
            doReturn(predicate).when(cb).lessThanOrEqualTo(any(Expression.class), any(Comparable.class));

            Predicate result = ProductSpecification.hasMaxPrice(100)
                    .toPredicate(root, query, cb);

            assertThat(result).isSameAs(predicate);
            verify(root).get("price");
            verify(cb).lessThanOrEqualTo(any(Expression.class), any(Comparable.class));
        }
    }

    @Nested
    @DisplayName("hasCategories()")
    class HasCategories {

        @Test
        void shouldReturnConjunction_whenCategoriesIsNull() {
            when(cb.conjunction()).thenReturn(conjunction);

            Predicate result = ProductSpecification.hasCategories(null)
                    .toPredicate(root, query, cb);

            assertThat(result).isSameAs(conjunction);
        }

        @Test
        void shouldReturnConjunction_whenCategoriesIsEmpty() {
            when(cb.conjunction()).thenReturn(conjunction);

            Predicate result = ProductSpecification.hasCategories(List.of())
                    .toPredicate(root, query, cb);

            assertThat(result).isSameAs(conjunction);
        }

        @SuppressWarnings("unchecked")
        @Test
        void shouldJoinCategoriesSet_andBuildEqualPredicatePerCategory() {
            Join<ProductEntity, CategoryEntity> join = mock(Join.class);
            Path<String> categoryNamePath = mock(Path.class);
            Expression<String> lowerCategoryName = mock(Expression.class);

            doReturn(join).when(root).join("categoriesSet");
            doReturn(categoryNamePath).when(join).get("name");
            when(cb.lower(categoryNamePath)).thenReturn(lowerCategoryName);
            when(cb.equal(lowerCategoryName, "streetwear")).thenReturn(predicate);
            when(cb.and(any(Predicate[].class))).thenReturn(predicate);
            when(query.getResultType()).thenReturn((Class) ProductEntity.class);

            ProductSpecification.hasCategories(List.of("Streetwear")).toPredicate(root, query, cb);

            verify(root).join("categoriesSet");
            verify(cb).equal(lowerCategoryName, "streetwear");
        }

        @SuppressWarnings("unchecked")
        @Test
        void shouldCallDistinct_whenQueryResultTypeIsNotLong() {
            Join<ProductEntity, CategoryEntity> join = mock(Join.class);
            doReturn(join).when(root).join("categoriesSet");
            when(query.getResultType()).thenReturn((Class) ProductEntity.class);

            ProductSpecification.hasCategories(List.of("Streetwear")).toPredicate(root, query, cb);

            verify(query).distinct(true);
        }

        @SuppressWarnings("unchecked")
        @Test
        void shouldNotCallDistinct_whenQueryResultTypeIsLong() {
            // Count queries use Long as result type — calling distinct on them causes issues.
            Join<ProductEntity, CategoryEntity> join = mock(Join.class);
            doReturn(join).when(root).join("categoriesSet");
            when(query.getResultType()).thenReturn((Class) Long.class);

            ProductSpecification.hasCategories(List.of("Streetwear")).toPredicate(root, query, cb);

            verify(query, never()).distinct(anyBoolean());
        }
    }
}
