package com.example.diamondstore.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.diamondstore.model.Diamond;

@Repository
public interface DiamondRepository extends JpaRepository<Diamond, String>, JpaSpecificationExecutor<Diamond> {

    Diamond findByDiamondID(String diamondID);

    List<Diamond> findByColor(String color);

    List<Diamond> findByDiamondNameLike(String diamondNamePattern);

    List<Diamond> findBydiamondEntryPriceBetween(BigDecimal minDiamondPrice, BigDecimal maxDiamondPrice);

    boolean existsByCertificationID(String certificationID);

    boolean existsByWarrantyID(String warrantyID);

    List<Diamond> findAllByCertificationIDIn(List<String> certificationIDs);

    List<Diamond> findAllByWarrantyIDIn(List<String> warrantyIDs);

    // get all diamonds has cartSize = ?, color = ?, clarity = ?
    List<Diamond> findAllByCaratSizeAndColorAndClarity(BigDecimal caratSize, String color, String clarity);

    List<Diamond> findAllByGrossDiamondPriceNotNull();
}
