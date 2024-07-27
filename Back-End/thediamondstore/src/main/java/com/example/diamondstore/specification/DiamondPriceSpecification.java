package com.example.diamondstore.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.example.diamondstore.model.DiamondPrice;

import javax.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class DiamondPriceSpecification {

    public static Specification<DiamondPrice> filterBy(String clarity, String color, BigDecimal caratSize) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (clarity != null) {
                predicates.add(criteriaBuilder.equal(root.get("clarity"), clarity));
            }
            if (color != null) {
                predicates.add(criteriaBuilder.equal(root.get("color"), color));
            }
            if (caratSize != null) {
                predicates.add(criteriaBuilder.equal(root.get("caratSize"), caratSize));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}