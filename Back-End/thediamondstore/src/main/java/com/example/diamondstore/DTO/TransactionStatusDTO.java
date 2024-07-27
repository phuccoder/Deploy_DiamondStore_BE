package com.example.diamondstore.DTO;

import java.io.Serializable;

public class TransactionStatusDTO implements Serializable {

    private String status;
    private String message;
    private String data;

    public TransactionStatusDTO() {
    }

    public TransactionStatusDTO(String data, String message, String status) {
        this.data = data;
        this.message = message;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
