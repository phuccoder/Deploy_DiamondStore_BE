package com.example.diamondstore.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.diamondstore.model.Jewelry;

@Repository
public interface JewelryRepository extends JpaRepository<Jewelry, String>, JpaSpecificationExecutor<Jewelry> {

    Jewelry findByJewelryID(String jewelryID);

    List<Jewelry> findByJewelryNameLike(String jewelryNamePattern);

    List<Jewelry> findByJewelryEntryPriceBetween(BigDecimal minjewelryEntryPrice, BigDecimal maxjewelryEntryPrice);

    boolean existsByWarrantyID(String warrantyID);

    List<Jewelry> findAllByWarrantyIDIn(List<String> warrantyIDs);
}
