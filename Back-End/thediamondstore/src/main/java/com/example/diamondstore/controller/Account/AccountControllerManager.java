package com.example.diamondstore.controller.Account;

import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.diamondstore.model.Account;
import com.example.diamondstore.request.AccountRequest;
import com.example.diamondstore.service.AccountService;

@RestController
@RequestMapping("/api/manager/accounts")
public class AccountControllerManager {

    private final AccountService accountService;

    public AccountControllerManager(AccountService accountService) {
        this.accountService = accountService;
    }

    @PutMapping(value = "/update/{accountID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> updateAccountManager(@PathVariable Integer accountID, @RequestBody AccountRequest accountRequest) {
        try {
            Account updatedAccount = accountService.updateAccount_Customer(accountID, accountRequest);
            return ResponseEntity.ok(Collections.singletonMap("message", "Cập nhật thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }
}
