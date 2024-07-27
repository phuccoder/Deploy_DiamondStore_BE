package com.example.diamondstore.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "DiamondPrice")
public class DiamondPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diamondPriceID")
    private Integer diamondPriceID;

    @Column(name = "weight", precision = 16, scale = 2, nullable = true)
    private BigDecimal weight;

    @Column(name = "diamondEntryPrice", precision = 16, scale = 2, nullable = true)
    private BigDecimal diamondEntryPrice;

    @Column(name = "clarity")
    private String clarity;

    @Column(name = "color")
    private String color;

    @Column(name = "caratSize", precision = 16, scale = 2)
    private BigDecimal caratSize;

    public DiamondPrice() {
    }

    public DiamondPrice(Integer diamondPriceID, BigDecimal weight, BigDecimal diamondEntryPrice, String clarity,
            String color, BigDecimal caratSize) {

        this.diamondPriceID = diamondPriceID;
        this.weight = weight;
        this.diamondEntryPrice = diamondEntryPrice;
        this.clarity = clarity;
        this.color = color;
        this.caratSize = caratSize;
    }

    public Integer getDiamondPriceID() {
        return diamondPriceID;
    }

    public void setDiamondPriceID(Integer diamondPriceID) {
        this.diamondPriceID = diamondPriceID;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
    this.weight = weight.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getDiamondEntryPrice() {
        return diamondEntryPrice;
    }

    public void setDiamondEntryPrice(BigDecimal diamondEntryPrice) {
        this.diamondEntryPrice = diamondEntryPrice;
    }

    public String getClarity() {
        return clarity;
    }

    public void setClarity(String clarity) {
        this.clarity = clarity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BigDecimal getCaratSize() {
        return caratSize;
    }

    public void setCaratSize(BigDecimal caratSize) {
        this.caratSize = caratSize;
    }

}
