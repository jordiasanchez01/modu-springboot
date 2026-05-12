package com.laberit.Modu.repositories.specifications;

import com.laberit.Modu.domain.model.ProductSearchCriteria;
import com.laberit.Modu.repositories.models.CategoryEntity;
import com.laberit.Modu.repositories.models.ProductEntity;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<ProductEntity> hasTitle(String title) {
        return (root, query, cb) -> {
            if (title == null) return cb.conjunction();
            String pattern = "%" + escapeLike(title.toLowerCase()) + "%";
            return cb.like(cb.lower(root.get("name")), pattern, '\\');
        };
    }

    public static Specification<ProductEntity> hasMaxPrice(Integer maxPrice) {
        return (root, query, cb) -> {
            if (maxPrice == null) return cb.conjunction();
            return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }

    public static Specification<ProductEntity> hasCategories(List<Integer> categoryIds) {
        return (root, query, cb) -> {
            if (categoryIds == null || categoryIds.isEmpty()) return cb.conjunction();
            if (Long.class != query.getResultType()) {
                query.distinct(true);
            }
            Join<ProductEntity, CategoryEntity> join = root.join("categoriesSet");
            return join.get("id").in(categoryIds);
        };
    }

    private static String escapeLike(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }
}
