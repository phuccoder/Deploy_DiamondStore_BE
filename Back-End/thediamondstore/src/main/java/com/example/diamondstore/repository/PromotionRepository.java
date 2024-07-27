package com.example.diamondstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.diamondstore.model.Promotion;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

    Promotion findByPromotionID(Integer promotionID);

    Promotion findByPromotionCode(String promotionCode);
}
