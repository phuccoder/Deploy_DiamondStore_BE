package com.example.diamondstore.request;

public class AccountRequest {
    
    private String accountName;
    private String password;
    private String role;
    private String phoneNumber;
    private String email;
    private String addressAccount;
    private Boolean active;

    public AccountRequest() {
    }

    public AccountRequest(String accountName, String password, String role, String phoneNumber, String email, String addressAccount, Boolean active) {
        this.accountName = accountName;
        this.password = password; 
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.addressAccount = addressAccount;
        this.active = active;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddressAccount() {
        return addressAccount;
    }

    public void setAddressAccount(String addressAccount) {
        this.addressAccount = addressAccount;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
