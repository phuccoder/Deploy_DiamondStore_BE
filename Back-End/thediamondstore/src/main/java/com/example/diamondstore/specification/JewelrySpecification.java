package com.example.diamondstore.specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.diamondstore.model.Jewelry;

public class JewelrySpecification {

    public static Specification<Jewelry> hasName(String jewelryName) {
        return (root, query, cb) -> cb.equal(root.get("jewelryName"), jewelryName);
    }

    public static Specification<Jewelry> hasPrice(Float jewelryEntryPrice) {
        return (root, query, cb) -> cb.equal(root.get("jewelryEntryPrice"), jewelryEntryPrice);
    }

    public static Specification<Jewelry> hasGender(String gender) {
        return (root, query, cb) -> cb.equal(root.get("gender"), gender);
    }

    public static Specification<Jewelry> hasNameLike(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("jewelryName"), "%" + name + "%");
    }

    public static Specification<Jewelry> hasJewelryNameIgnoreCase(String jewelryName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("jewelryName")), "%" + jewelryName.toLowerCase() + "%");
    }

    public static Specification<Jewelry> hasMaxJewelryEntryPrice(Float maxJewelryEntryPrice) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("jewelryEntryPrice"), maxJewelryEntryPrice);
    }

    public static Specification<Jewelry> hasMinJewelryEntryPrice(Float minJewelryEntryPrice) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("jewelryEntryPrice"), minJewelryEntryPrice);
    }
}
