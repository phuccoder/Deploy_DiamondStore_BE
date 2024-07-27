package com.example.diamondstore.request.putRequest;

public class CartPutRequest {

    private Integer accountID;
    private String diamondID;
    private String jewelryID;
    private Integer quantity;
    private float totalPrice;

    public CartPutRequest() {
    }

    public CartPutRequest(Integer accountID, String diamondID, String jewelryID, Integer quantity, float totalPrice) {
        this.accountID = accountID;
        this.diamondID = diamondID;
        this.jewelryID = jewelryID;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public String getDiamondID() {
        return diamondID;
    }

    public void setDiamondID(String diamondID) {
        this.diamondID = diamondID;
    }

    public String getJewelryID() {
        return jewelryID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
}
