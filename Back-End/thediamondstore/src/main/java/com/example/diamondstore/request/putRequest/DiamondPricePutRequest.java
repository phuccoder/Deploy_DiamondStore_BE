package com.example.diamondstore.request.putRequest;

import java.math.BigDecimal;

public class DiamondPricePutRequest {
    
    private BigDecimal weight;
    private String clarity;
    private String color;
    private BigDecimal caratSize;
    private BigDecimal diamondEntryPrice;


    public DiamondPricePutRequest() {
    }

    public DiamondPricePutRequest(BigDecimal weight, String clarity, String color, BigDecimal caratSize, BigDecimal diamondEntryPrice) {
        this.weight = weight;
        this.clarity = clarity;
        this.color = color;
        this.caratSize = caratSize;
        this.diamondEntryPrice = diamondEntryPrice;
    }

    public BigDecimal getweight() {
        return weight;
    }

    public void setweight(BigDecimal weight) {
        this.weight = weight;
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


    public BigDecimal getDiamondEntryPrice() {
        return diamondEntryPrice;
    }

    public void setDiamondEntryPrice(BigDecimal diamondEntryPrice) {
        this.diamondEntryPrice = diamondEntryPrice;
    }
}
