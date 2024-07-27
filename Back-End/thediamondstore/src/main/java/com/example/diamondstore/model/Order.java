package com.example.diamondstore.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder.In;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderID")
    private Integer orderID;

    @ManyToOne
    @JoinColumn(name = "accountID", nullable = false)
    private Account account;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "startorderDate", nullable = false)
    private LocalDateTime startorderDate;

    @Column(name = "orderStatus")
    private String orderStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "deliveryDate", nullable = false)
    private LocalDateTime deliveryDate;

    @Column(name = "totalOrder", precision = 18, scale = 2)
    private BigDecimal totalOrder;

    @Column(name = "deliveryAddress")
    private String deliveryAddress;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "promotionCode")
    private String promotionCode; 

    @Column(name = "accountPoint")
    private Integer accountPoint;

    @Column(name = "transactionNo")
    private Integer transactionNo;

    @JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    @JsonIgnore
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<Cart> cartItems;


    public Order() {
    }

    public Order(Integer orderID, Account account, LocalDateTime startorderDate, String orderStatus,
            LocalDateTime deliveryDate, BigDecimal totalOrder, String deliveryAddress, String phoneNumber,
            String promotionCode, Integer transactionNo, List<OrderDetail> orderDetails, Integer accountPoint) {
        this.orderID = orderID;
        this.account = account;
        this.startorderDate = startorderDate;
        this.orderStatus = orderStatus;
        this.deliveryDate = deliveryDate;
        this.totalOrder = totalOrder;
        this.deliveryAddress = deliveryAddress;
        this.phoneNumber = phoneNumber;
        this.promotionCode = promotionCode;
        this.transactionNo = transactionNo;
        this.orderDetails = orderDetails;
        this.accountPoint = accountPoint;
    }

    public Integer getOrderID() {
        return orderID;
    }

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LocalDateTime getStartorderDate() {
        return startorderDate;
    }

    public void setStartorderDate(LocalDateTime startorderDate) {
        this.startorderDate = startorderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public BigDecimal getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(BigDecimal totalOrder) {
        this.totalOrder = totalOrder;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public Integer getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(Integer transactionNo) {
        this.transactionNo = transactionNo;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public List<Cart> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<Cart> cartItems) {
        this.cartItems = cartItems;
    }
    
    public Integer getAccountPoint() {
        return accountPoint;
    }

    public void setAccountPoint(Integer accountPoint) {
        this.accountPoint = accountPoint;
    }
    
}
