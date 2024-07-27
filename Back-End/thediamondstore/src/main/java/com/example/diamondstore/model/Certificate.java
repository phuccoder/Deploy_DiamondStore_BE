package com.example.diamondstore.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "Certificate")
public class Certificate {

    @Id
    @Column(name = "certificateID")
    private String certificateID;

    @Column(name = "diamondID", nullable = false)
    private String diamondID;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "expirationDate", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "certificateImage")
    private String certificateImage;

    @Column(name = "certificateStatus")
    private String certificateStatus;

    public Certificate() {
    }

    public Certificate(String certificateID, String diamondID, LocalDateTime expirationDate, String certificateImage, String certificateStatus) {
        this.certificateID = certificateID;
        this.diamondID = diamondID;
        this.expirationDate = expirationDate;
        this.certificateImage = certificateImage;
        this.certificateStatus = certificateStatus;
    }

    public String getCertificateID() {
        return certificateID;
    }

    public void setCertificateID(String certificateID) {
        this.certificateID = certificateID;
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

    public String getcertificateImage() {
        return certificateImage;
    }

    public void setcertificateImage(String certificateImage) {
        this.certificateImage = certificateImage;
    }

    public String getCertificateStatus() {
        return certificateStatus;
    }

    public void setCertificateStatus(String certificateStatus) {
        this.certificateStatus = certificateStatus;
    }
}
