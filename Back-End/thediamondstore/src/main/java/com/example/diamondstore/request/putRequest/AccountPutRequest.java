package com.example.diamondstore.request.putRequest;

public class AccountPutRequest {

    private String email;
    private String accountName;
    private String password;
    private String phoneNumber;
    private String role;

    public AccountPutRequest() {
    }

    public AccountPutRequest(String email, String accountName, String password, String phoneNumber, String role) {
        this.email = email;
        this.accountName = accountName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
