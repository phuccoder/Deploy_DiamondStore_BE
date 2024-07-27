package com.example.diamondstore.request.putRequest;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class WarrantyPutRequest {

    private String diamondID;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expirationDate;
    private String warrantyImage;

    public WarrantyPutRequest() {
    }

    public WarrantyPutRequest(String diamondID, LocalDateTime expirationDate, String warrantyImage) {
        this.diamondID = diamondID;
        this.expirationDate = expirationDate;
        this.warrantyImage = warrantyImage;
    }

    public String getDiamondID() {
        return diamondID;
    }

    public void setDiamondID(String diamondID) {
        this.diamondID = diamondID;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getWarrantyImage() {
        return warrantyImage;
    }

    public void setWarrantyImage(String warrantyImage) {
        this.warrantyImage = warrantyImage;
    }
}
