package com.example.diamondstore.request.putRequest;

import java.math.BigDecimal;

import javax.persistence.criteria.CriteriaBuilder.In;

public class JewelryPutRequest {

    private String jewelryName;
    private String gender;
    private String jewelryImage;
    private BigDecimal jewelryEntryPrice;
    private String warrantyID;
    private Integer quantity;

    public JewelryPutRequest() {
    }

    public JewelryPutRequest(String jewelryName, String gender, String jewelryImage, BigDecimal jewelryEntryPrice, String warrantyID, Integer quantity) {
        this.jewelryName = jewelryName;
        this.gender = gender;
        this.jewelryImage = jewelryImage;
        this.jewelryEntryPrice = jewelryEntryPrice;
        this.warrantyID = warrantyID;
        this.quantity = quantity;
    }

    public String getJewelryName() {
        return jewelryName;
    }

    public void setJewelryName(String jewelryName) {
        this.jewelryName = jewelryName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getJewelryImage() {
        return jewelryImage;
    }

    public void setJewelryImage(String jewelryImage) {
        this.jewelryImage = jewelryImage;
    }

    public BigDecimal getJewelryEntryPrice() {
        return jewelryEntryPrice;
    }

    public void setJewelryEntryPrice(BigDecimal jewelryEntryPrice) {
        this.jewelryEntryPrice = jewelryEntryPrice;
    }

    public String getWarrantyID() {
        return warrantyID;
    }

    public void setWarrantyID(String warrantyID) {
        this.warrantyID = warrantyID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
