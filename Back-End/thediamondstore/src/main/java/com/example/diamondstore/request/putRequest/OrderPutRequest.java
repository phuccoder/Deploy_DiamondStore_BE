package com.example.diamondstore.request.putRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OrderPutRequest {

    private String orderStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deliveryDate;
    private String deliveryAddress;
    private String promotionCode;
    private BigDecimal totalOrder;

    public OrderPutRequest() {
    }

    public OrderPutRequest(String orderStatus, LocalDateTime deliveryDate, String deliveryAddress, String promotionCode,
            BigDecimal totalOrder) {
        this.orderStatus = orderStatus;
        this.deliveryDate = deliveryDate;
        this.deliveryAddress = deliveryAddress;
        this.promotionCode = promotionCode;
        this.totalOrder = totalOrder;
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

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public BigDecimal getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(BigDecimal totalOrder) {
        this.totalOrder = totalOrder;
    }
}
