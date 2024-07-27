package com.example.diamondstore.controller.Account;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.diamondstore.model.Account;
import com.example.diamondstore.request.AccountRequest;
import com.example.diamondstore.service.AccountService;


@RestController
@RequestMapping("/api/admin/account-management/accounts")
public class AccountControllerAdmin {

    private final AccountService accountService;

    public AccountControllerAdmin(AccountService accountService) {
        this.accountService = accountService;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteAccounts(@RequestBody List<Integer> accountIDs) {
        return accountService.deleteAccounts(accountIDs);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> createAccount_Admin(@RequestBody AccountRequest accountRequest) {
        try {
            accountService.createAccount(accountRequest);
            return ResponseEntity.ok(Collections.singletonMap("message", "Tạo tài khoản thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @PutMapping(value = "/update/{accountID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> updateAccount_Admin(@PathVariable Integer accountID, @RequestBody AccountRequest accountRequest) {
        try {
            Account updatedAccount = accountService.updateAccount_Admin(accountID, accountRequest);
            return ResponseEntity.ok(Collections.singletonMap("message", "Cập nhật thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }
}
