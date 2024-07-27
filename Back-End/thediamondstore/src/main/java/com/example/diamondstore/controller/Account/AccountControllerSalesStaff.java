package com.example.diamondstore.controller.Account;

import java.util.Collections;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.diamondstore.model.Account;
import com.example.diamondstore.repository.AccountRepository;
import com.example.diamondstore.request.AccountRequest;
import com.example.diamondstore.service.AccountService;

@RestController
@RequestMapping("/api/sales-staff/accounts")
public class AccountControllerSalesStaff {

    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public AccountControllerSalesStaff(AccountRepository accountRepository, AccountService accountService) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    @GetMapping("/{accountID}")
    public ResponseEntity<?> getByAccountID(@PathVariable Integer accountID) {
        Account account = accountRepository.findByAccountID(accountID);
        if (account == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Không tìm thấy tài khoản"));
        }
        return ResponseEntity.ok(account);
    }

    @GetMapping("/get-by-email/{email}")
    public ResponseEntity<?> getByAccountEmail(@PathVariable String email) {
        Optional<Account> account = accountRepository.findByEmail(email);
        if (account.isPresent()) {
            return ResponseEntity.ok(account.get());
        } else {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Không tìm thấy tài khoản"));
        }
    }

    @PutMapping(value = "/update/{accountID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> updateAccount(@PathVariable Integer accountID, @RequestBody AccountRequest accountRequest) {
        try {
            Account updatedAccount = accountService.updateAccount_Customer(accountID, accountRequest);
            return ResponseEntity.ok(Collections.singletonMap("message", "Cập nhật thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }
}
