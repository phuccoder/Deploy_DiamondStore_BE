package com.example.diamondstore.DTO;

public class AccountContactInfoDTO {
    
    private String phoneNumber;
    private String addressAccount;

    public AccountContactInfoDTO(String phoneNumber, String addressAccount) {
        this.phoneNumber = phoneNumber;
        this.addressAccount = addressAccount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddressAccount() {
        return addressAccount;
    }

    public void setAddressAccount(String addressAccount) {
        this.addressAccount = addressAccount;
    }
}
