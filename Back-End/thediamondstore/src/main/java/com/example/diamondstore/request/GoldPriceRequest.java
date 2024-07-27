package com.example.diamondstore.request;

import java.math.BigDecimal;

public class GoldPriceRequest {
    
    private BigDecimal goldPrice;
    private String jewelryID;
    private int goldAge;

    public GoldPriceRequest() {
    }

    public GoldPriceRequest(BigDecimal goldPrice, String jewelryID, int goldAge) {
        this.goldPrice = goldPrice;
        this.jewelryID = jewelryID;
        this.goldAge = goldAge;
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

    public int getGoldAge() {
        return goldAge;
    }

    public void setGoldAge(int goldAge) {
        this.goldAge = goldAge;
    }

    
}
