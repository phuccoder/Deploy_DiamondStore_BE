package com.example.diamondstore.model;

import java.math.BigDecimal;
import javax.persistence.*;

@Entity
@Table(name = "Cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartID;

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
    @JoinColumn(name = "orderID", nullable = true) 
    private Order order;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "totalPrice", nullable = false, precision = 8, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "sizeJewelry", nullable = false)
    private Integer sizeJewelry;

    @Column(name = "price", nullable = false, precision = 8, scale = 2)
    private BigDecimal price;

    @Column(name = "grossCartPrice", precision = 8, scale = 2)
    private BigDecimal grossCartPrice;

    @Column(name = "cartStatus")
    private String cartStatus;
    public Cart() {
    }

    public Cart(Account account, Integer cartID, Diamond diamond, Jewelry jewelry, Order order, Integer quantity,
            BigDecimal totalPrice, Integer sizeJewelry, BigDecimal price, BigDecimal grossCartPrice, String cartStatus) {
        this.account = account;
        this.cartID = cartID;
        this.diamond = diamond;
        this.jewelry = jewelry;
        this.order = order;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.sizeJewelry = sizeJewelry;
        this.price = price;
        this.grossCartPrice = grossCartPrice;
        this.cartStatus = cartStatus;
    }

    public BigDecimal getGrossCartPrice() {
        return grossCartPrice;
    }

    public void setGrossCartPrice(BigDecimal grossCartPrice) {
        this.grossCartPrice = grossCartPrice;
    }

    public Integer getSizeJewelry() {
        return sizeJewelry;
    }

    public void setSizeJewelry(Integer sizeJewelry) {
        this.sizeJewelry = sizeJewelry;
    }

    public Integer getCartID() {
        return cartID;
    }

    public void setCartID(Integer cartID) {
        this.cartID = cartID;
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCartStatus() {
        return cartStatus;
    }

    public void setCartStatus(String cartStatus) {
        this.cartStatus = cartStatus;
    }
}
