package com.example.diamondstore.request;

import java.math.BigDecimal;

public class DiamondPriceRequest {
    
    private BigDecimal weight;
    private BigDecimal diamondEntryPrice;
    private String clarity;
    private String color;
    private BigDecimal caratSize;

    public DiamondPriceRequest() {
    }

    public DiamondPriceRequest(BigDecimal weight, BigDecimal diamondEntryPrice, String clarity, String color, BigDecimal caratSize) {
        this.weight = weight;
        this.diamondEntryPrice = diamondEntryPrice;
        this.clarity = clarity;
        this.color = color;
        this.caratSize = caratSize;
    }

    public BigDecimal getweight() {
        return weight;
    }

    public void setweight(BigDecimal weight) {
        this.weight = weight;
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
