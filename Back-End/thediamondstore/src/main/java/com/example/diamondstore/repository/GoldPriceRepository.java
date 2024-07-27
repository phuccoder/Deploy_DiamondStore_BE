package com.example.diamondstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.diamondstore.model.GoldPrice;

public interface GoldPriceRepository extends JpaRepository<GoldPrice, Integer>{
    
    GoldPrice findByGoldPriceID(Integer goldPriceID);

    GoldPrice findByJewelryID(String jewelryID);

    void deleteByJewelryID(String jewelryID);

}
