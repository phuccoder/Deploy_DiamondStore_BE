package com.example.diamondstore.request;

public class ApplyPromotionRequest {

    private String promotionCode;

    public ApplyPromotionRequest() {
    }

    public ApplyPromotionRequest(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }
}
