package com.example.diamondstore.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "OrderDetail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderDetailID")
    private Integer orderDetailID;

    @ManyToOne
    @JoinColumn(name = "orderID", nullable = false)
    @JsonBackReference
    private Order order;

    @ManyToOne
    @JoinColumn(name = "accountID", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "diamondID", nullable = true)
    private Diamond diamond;

    @ManyToOne
    @JoinColumn(name = "jewelryID", nullable = true)
    private Jewelry jewelry;

    @ManyToOne
    @JoinColumn(name = "promotionID", nullable = true)
    private Promotion promotion;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "sizeJewelry")
    private Integer sizeJewelry;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "grossCartPrice", nullable = false)
    private BigDecimal grossCartPrice;

    @Column(name = "totalPrice", nullable = false)
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "warrantyID", nullable = true)
    @JsonManagedReference
    private Warranty warranty;

    @Column(name = "diamondCertificateImage", nullable = true)
    private String diamondCertificateImage;

    public OrderDetail() {
    }

    public OrderDetail(Integer orderDetailID, Order order, Account account, Diamond diamond, Jewelry jewelry,
            Integer quantity, Integer sizeJewelry, BigDecimal price, BigDecimal grossCartPrice, BigDecimal totalPrice,
            String diamondCertificateImage, Promotion promotion, Warranty warranty) {
        this.orderDetailID = orderDetailID;
        this.order = order;
        this.account = account;
        this.diamond = diamond;
        this.jewelry = jewelry;
        this.quantity = quantity;
        this.sizeJewelry = sizeJewelry;
        this.price = price;
        this.grossCartPrice = grossCartPrice;
        this.totalPrice = totalPrice;
        this.diamondCertificateImage = diamondCertificateImage;
        this.promotion = promotion;
        this.warranty = warranty;
    }

    public Integer getOrderDetailID() {
        return orderDetailID;
    }

    public void setOrderDetailID(Integer orderDetailID) {
        this.orderDetailID = orderDetailID;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Diamond getDiamond() {
        return diamond;
    }

    public void setDiamond(Diamond diamond) {
        this.diamond = diamond;
    }

    public Jewelry getJewelry() {
        return jewelry;
    }

    public void setJewelry(Jewelry jewelry) {
        this.jewelry = jewelry;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getSizeJewelry() {
        return sizeJewelry;
    }

    public void setSizeJewelry(Integer sizeJewelry) {
        this.sizeJewelry = sizeJewelry;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getGrossCartPrice() {
        return grossCartPrice;
    }

    public void setGrossCartPrice(BigDecimal grossCartPrice) {
        this.grossCartPrice = grossCartPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDiamondCertificateImage() {
        return diamondCertificateImage;
    }

    public void setDiamondCertificateImage(String diamondCertificateImage) {
        this.diamondCertificateImage = diamondCertificateImage;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public Warranty getWarranty() {
        return warranty;
    }

    public void setWarranty(Warranty warranty) {
        this.warranty = warranty;
    }

}
