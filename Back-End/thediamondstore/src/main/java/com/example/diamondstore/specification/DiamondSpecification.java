package com.example.diamondstore.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.example.diamondstore.model.Diamond;

public class DiamondSpecification {
    public static Specification<Diamond> hasOrigin(String origin) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("origin"), origin);
    }

    public static Specification<Diamond> hasCut(String cut) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("cut"), cut);
    }

    public static Specification<Diamond> hasColor(String color) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("color"), color);
    }

    public static Specification<Diamond> hasShape(String shape) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("shape"), shape);
    }

    public static Specification<Diamond> hasMinCaratSize(BigDecimal minCaratSize) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("caratSize"), minCaratSize);
    }

    public static Specification<Diamond> hasMaxCaratSize(BigDecimal maxCaratSize) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("caratSize"), maxCaratSize);
    }
    
    public static Specification<Diamond> hasMinCaratWeight(BigDecimal minWeight) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("weight"), minWeight);
    }

    public static Specification<Diamond> hasMaxCaratWeight(BigDecimal maxWeight) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("weight"), maxWeight);
    }

    public static Specification<Diamond> hasMinDiamondEntryPrice(Float mindiamondEntryPrice) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("diamondEntryPrice"), mindiamondEntryPrice);
    }

    public static Specification<Diamond> hasMaxDiamondEntryPrice(Float maxdiamondEntryPrice) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("diamondEntryPrice"), maxdiamondEntryPrice);
    }

    public static Specification<Diamond> hasClarity(String clarity) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("clarity"), clarity);
    }

    public static Specification<Diamond> hasDiamondNameLike(String diamondNameLike) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("diamondName"), "%" + diamondNameLike + "%");
    }

    public static Specification<Diamond> hasDiamondNameIgnoreCase(String diamondName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("diamondName")), "%" + diamondName.toLowerCase() + "%");
    }

    
}
