package com.example.diamondstore.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "WarrantyHistory")
public class WarrantyHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warrantyHistoryID", nullable = false)
    private Integer warrantyHistoryID;

    @ManyToOne
    @JoinColumn(name = "warrantyID", nullable = false)
    @JsonBackReference
    private Warranty warranty;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "effectiveDate", nullable = false)
    private LocalDateTime effectiveDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "expirationDate", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "warrantyStatus")
    private String warrantyStatus;

    public WarrantyHistory() {
    }

    public WarrantyHistory(Integer warrantyHistoryID, Warranty warranty, LocalDateTime effectiveDate,
            LocalDateTime expirationDate, String warrantyStatus) {
        this.warrantyHistoryID = warrantyHistoryID;
        this.warranty = warranty;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
        this.warrantyStatus = warrantyStatus;
    }

    public Integer getWarrantyHistoryID() {
        return warrantyHistoryID;
    }

    public void setWarrantyHistoryID(Integer warrantyHistoryID) {
        this.warrantyHistoryID = warrantyHistoryID;
    }

    public Warranty getWarranty() {
        return warranty;
    }

    public void setWarranty(Warranty warranty) {
        this.warranty = warranty;
    }

    public LocalDateTime getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getWarrantyStatus() {
        return warrantyStatus;
    }

    public void setWarrantyStatus(String warrantyStatus) {
        this.warrantyStatus = warrantyStatus;
    }
    
    
}
