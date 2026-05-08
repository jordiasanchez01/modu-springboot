package com.laberit.Modu.repositories.specifications;

import com.laberit.Modu.domain.model.ProductSearchCriteria;
import com.laberit.Modu.repositories.models.CategoryEntity;
import com.laberit.Modu.repositories.models.ProductEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<ProductEntity> getSpecification(ProductSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.title() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + criteria.title() + "%"));
            }

            if (criteria.categoryIds() != null && !criteria.categoryIds().isEmpty()) {
                for (Integer categoryId : criteria.categoryIds()) {
                    Join<ProductEntity, CategoryEntity> categoriesJoin = root.join("categoriesList");
                    predicates.add(criteriaBuilder.equal(categoriesJoin.get("id"), categoryId));
                }
                query.distinct(true);
            }

            if (criteria.maxPrice() != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("price"), criteria.maxPrice()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
