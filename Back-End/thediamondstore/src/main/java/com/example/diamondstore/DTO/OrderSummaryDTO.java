package com.example.diamondstore.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OrderSummaryDTO {

    private LocalDate date;
    private BigDecimal revenue;

    // constructor
    public OrderSummaryDTO(LocalDate date, BigDecimal revenue) {
        this.date = date;
        this.revenue = revenue;
    }

    // getters and setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getrevenue() {
        return revenue;
    }

    public void setrevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }
}
