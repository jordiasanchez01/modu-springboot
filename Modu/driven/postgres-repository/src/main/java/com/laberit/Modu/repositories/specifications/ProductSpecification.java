package com.laberit.Modu.repositories.specifications;

import com.laberit.Modu.repositories.models.CategoryEntity;
import com.laberit.Modu.repositories.models.ProductEntity;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<ProductEntity> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null) return criteriaBuilder.conjunction();
            String pattern = "%" + escapeLike(title.toLowerCase()) + "%";
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern, '\\');
        };
    }

    public static Specification<ProductEntity> hasMaxPrice(Integer maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (maxPrice == null) return criteriaBuilder.conjunction();
            return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }

    public static Specification<ProductEntity> hasCategories(List<String> categories) {
        return (root, query, criteriaBuilder) -> {
            if (categories == null || categories.isEmpty()) return criteriaBuilder.conjunction();
            List<Predicate> predicates = new ArrayList<>();
            for (String category : categories) {
                Join<ProductEntity, CategoryEntity> join = root.join("categoriesSet");
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(join.get("name")), category.toLowerCase()));
            }
            if (Long.class != query.getResultType()) {
                query.distinct(true);
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static String escapeLike(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }
}
