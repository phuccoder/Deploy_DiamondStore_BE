package com.example.diamondstore.request.putRequest;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CertificatePutRequest {

    private String diamondID;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expirationDate;
    private String certificateImage;

    public CertificatePutRequest() {
    }

    public CertificatePutRequest(String diamondID, LocalDateTime expirationDate, String certificateImage) {
        this.diamondID = diamondID;
        this.expirationDate = expirationDate;
        this.certificateImage = certificateImage;
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

    public String getCertificateImage() {
        return certificateImage;
    }

    public void setCertificateImage(String certificateImage) {
        this.certificateImage = certificateImage;
    }
}
