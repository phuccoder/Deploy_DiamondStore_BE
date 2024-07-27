package com.example.diamondstore.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.FetchType;


@Entity
@Table(name = "Diamond")
public class Diamond {

    @Id
    @Column(name = "diamondID")
    private String diamondID;

    @Column(name = "warrantyID", nullable = true)
    private String warrantyID;

    @Column(name = "certificationID", nullable = true)
    private String certificationID;

    @Column(name = "caratSize", precision = 16, scale = 2)
    private BigDecimal caratSize;

    @Column(name = "diamondEntryPrice", precision = 16, scale = 2, nullable = true)
    private BigDecimal diamondEntryPrice;

    @Column(name = "grossDiamondPrice", precision = 16, scale = 2, nullable = true)
    private BigDecimal grossDiamondPrice;

    @Column(name = "weight", precision = 16, scale = 2, nullable = true)
    private BigDecimal weight;

    @Column(name = "color")
    private String color;

    @Column(name = "cut")
    private String cut;

    @Column(name = "clarity")
    private String clarity;

    @Column(name = "diamondImage")
    private String diamondImage;

    @Column(name = "shape")
    private String shape;

    @Column(name = "diamondName")
    private String diamondName;

    @Column(name = "origin")
    private String origin;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "diamondPriceID", nullable = true)
    private DiamondPrice diamondPrice;

    public Diamond() {
    }

    public Diamond(String diamondID, String warrantyID, String certificationID, BigDecimal caratSize, BigDecimal diamondEntryPrice, BigDecimal weight, String color, String cut, String clarity, String diamondImage, String shape, String diamondName, String origin, BigDecimal grossDiamondPrice, Integer quantity, String status, DiamondPrice diamondPrice) {
        this.diamondID = diamondID;
        this.warrantyID = warrantyID;
        this.certificationID = certificationID;
        this.caratSize = caratSize;
        this.diamondEntryPrice = diamondEntryPrice;
        this.weight = weight;
        this.color = color;
        this.cut = cut;
        this.clarity = clarity;
        this.diamondImage = diamondImage;
        this.shape = shape;
        this.diamondName = diamondName;
        this.origin = origin;
        this.grossDiamondPrice = grossDiamondPrice;
        this.quantity = quantity;
        this.status = status;
        this.diamondPrice = diamondPrice;
    }

    public BigDecimal getGrossDiamondPrice() {
        return grossDiamondPrice;
    }

    public void setGrossDiamondPrice(BigDecimal grossDiamondPrice) {
        this.grossDiamondPrice = grossDiamondPrice;
    }

    public String getDiamondID() {
        return diamondID;
    }

    public void setDiamondID(String diamondID) {
        this.diamondID = diamondID;
    }

    public String getWarrantyID() {
        return warrantyID;
    }

    public void setWarrantyID(String warrantyID) {
        this.warrantyID = warrantyID;
    }

    public String getCertificationID() {
        return certificationID;
    }

    public void setCertificationID(String certificationID) {
        this.certificationID = certificationID;
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

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
    this.weight = weight.setScale(2, RoundingMode.HALF_UP);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCut() {
        return cut;
    }

    public void setCut(String cut) {
        this.cut = cut;
    }

    public String getClarity() {
        return clarity;
    }

    public void setClarity(String clarity) {
        this.clarity = clarity;
    }

    public String getDiamondImage() {
        return diamondImage;
    }

    public void setDiamondImage(String diamondImage) {
        this.diamondImage = diamondImage;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getDiamondName() {
        return diamondName;
    }

    public void setDiamondName(String diamondName) {
        this.diamondName = diamondName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DiamondPrice getDiamondPrice() {
        return diamondPrice;
    }

    public void setDiamondPrice(DiamondPrice diamondPrice) {
        this.diamondPrice = diamondPrice;
    }
}
