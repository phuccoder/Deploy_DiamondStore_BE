package com.example.diamondstore.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "GoldPrice")
public class GoldPrice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goldPriceID")
    private Integer goldPriceID;

    @Column(name = "goldPrice", precision=16, scale=2)
    private BigDecimal goldPrice;
    
    @Column(name = "jewelryID")
    private String jewelryID;

    @Column(name = "goldAge")
    private Integer goldAge;

    public GoldPrice() {
    }

    public GoldPrice(Integer goldPriceID, BigDecimal goldPrice, String jewelryID, Integer goldAge) {
        this.goldPriceID = goldPriceID;
        this.goldPrice = goldPrice;
        this.jewelryID = jewelryID;
        this.goldAge = goldAge;
    }

    public Integer getGoldpriceID() {
        return goldPriceID;
    }

    public void setGoldpriceID(Integer goldPriceID) {
        this.goldPriceID = goldPriceID;
    }

    public BigDecimal getGoldPrice() {
        return goldPrice;
    }

    public void setGoldPrice(BigDecimal goldPrice) {
        this.goldPrice = goldPrice;
    }

    public String getJewelryID() {
        return jewelryID;
    }

    public void setJewelryID(String jewelryID) {
        this.jewelryID = jewelryID;
    }

    public Integer getGoldAge() {
        return goldAge;
    }

    public void setGoldAge(Integer goldAge) {
        this.goldAge = goldAge;
    }

    
}
