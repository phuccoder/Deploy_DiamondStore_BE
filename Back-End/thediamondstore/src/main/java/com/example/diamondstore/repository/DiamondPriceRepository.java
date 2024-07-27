package com.example.diamondstore.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.diamondstore.model.DiamondPrice;

public interface  DiamondPriceRepository extends  JpaRepository<DiamondPrice, Integer>, JpaSpecificationExecutor<DiamondPrice> {

    DiamondPrice findByDiamondPriceID(Integer diamondPriceID);

    List<DiamondPrice> findByCaratSize(BigDecimal caratSize);

    // get diamondPrice has cartSize = ?, color = ?, clarity = ?
    DiamondPrice findAllByCaratSizeAndColorAndClarity(BigDecimal caratSize, String color, String clarity);
  
    boolean existsByCaratSizeAndColorAndClarity(BigDecimal caratSize, String color, String clarity);

    boolean existsByCaratSizeAndColorAndClarityAndDiamondPriceIDNot(BigDecimal caratSize, String color, String clarity, Integer diamondPriceID);
}
