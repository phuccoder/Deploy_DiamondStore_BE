package com.example.diamondstore.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "Account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accountID")
    private Integer accountID;

    @Column(name = "accountName", nullable = false)
    private String accountName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "addressAccount", nullable = false)
    private String addressAccount;

    @Column(name = "otp")
    private String otp;

    @Column(name = "otp_generated_time")
    private LocalDateTime otp_generated_time;

    @Column(name = "active")
    private boolean active;

    @JsonBackReference
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private AccumulatePoints accumulatePoints;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    @Transient
    private String unencryptedPassword; 

    public Account() {
    }

    public Account(Integer accountID, String accountName, String password, String role, String phoneNumber, String email, String addressAccount,
            String otp, LocalDateTime otp_generated_time, boolean active) {
        // Hàm khởi tạo với các tham số
        this.accountID = accountID;
        this.accountName = accountName;
        setPassword(password); 
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.addressAccount = addressAccount;
        this.otp = otp;
        this.otp_generated_time = otp_generated_time;
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getOtpGeneratedTime() {
        return otp_generated_time;
    }

    public void setOtpGeneratedTime(LocalDateTime otp_generated_time) {
        this.otp_generated_time = otp_generated_time;
    }

    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public String getPassword() {
        return password;
    }

    public String getAddressAccount() {
        return addressAccount;
    }

    public void setAddressAccount(String addressAccount) {
        this.addressAccount = addressAccount;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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

    public AccumulatePoints getAccumulatePoints() {
        return accumulatePoints;
    }

    public void setAccumulatePoints(AccumulatePoints accumulatePoints) {
        this.accumulatePoints = accumulatePoints;
    }
}
