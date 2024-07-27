package com.example.diamondstore.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

@Entity
@Table(name = "Warranty")
public class Warranty {

    @Id
    @Column(name = "warrantyID")
    private String warrantyID;

    @Column(name = "diamondID", nullable = true)
    private String diamondID;

    @Column(name = "warrantyImage")
    private String warrantyImage;

    @Column(name = "jewelryID", nullable = true)
    private String jewelryID;

    @OneToMany(mappedBy = "warranty", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<WarrantyHistory> warrantyHistories;

    public Warranty() {
    }

    public Warranty(String warrantyID, String diamondID, String warrantyImage, String jewelryID) {
        this.warrantyID = warrantyID;
        this.diamondID = diamondID;
        this.warrantyImage = warrantyImage;
        this.jewelryID = jewelryID;
    }

    public String getWarrantyID() {
        return warrantyID;
    }

    public void setWarrantyID(String warrantyID) {
        this.warrantyID = warrantyID;
    }

    public String getDiamondID() {
        return diamondID;
    }

    public void setDiamondID(String diamondID) {
        this.diamondID = diamondID;
    }

    public String getWarrantyImage() {
        return warrantyImage;
    }

    public void setWarrantyImage(String warrantyImage) {
        this.warrantyImage = warrantyImage;
    }

    public String getJewelryID() {
        return jewelryID;
    }

    public void setJewelryID(String jewelryID) {
        this.jewelryID = jewelryID;
    }

    public List<WarrantyHistory> getWarrantyHistories() {
        return warrantyHistories;
    }

    public void setWarrantyHistories(List<WarrantyHistory> warrantyHistories) {
        this.warrantyHistories = warrantyHistories;
    }
}
