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

import com.example.diamondstore.DTO.AccountContactInfoDTO;
import com.example.diamondstore.model.Account;
import com.example.diamondstore.model.Order;
import com.example.diamondstore.repository.AccountRepository;
import com.example.diamondstore.repository.OrderRepository;
import com.example.diamondstore.request.AccountRequest;
import com.example.diamondstore.service.AccountService;
import com.example.diamondstore.service.OrderService;

@RestController
@RequestMapping("/api/customer/accounts")
public class AccountControllerCustomer {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public AccountControllerCustomer(AccountRepository accountRepository, AccountService accountService, OrderService orderService, OrderRepository orderRepository) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/get-by-name/{accountName}")
    public ResponseEntity<Account> getByAccountName(@PathVariable String accountName) {
        Account account = accountRepository.findByAccountName(accountName);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
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

    //get phoneNumber, addressAccount by accountID
    @GetMapping("/contact-information/{accountID}")
    public ResponseEntity<?> getContactInfoByAccountID(@PathVariable Integer accountID) {
        Optional<Account> accountOpt = accountRepository.findById(accountID);
        if (accountOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Không tìm thấy tài khoản"));
        }

        Account account = accountOpt.get();
        String phoneNumber = account.getPhoneNumber();
        String address = account.getAddressAccount();

        // Check if phoneNumber and address are null
        if (phoneNumber == null || address == null) {
            Integer orderNo = orderService.LIFO(accountID);
            if (orderNo == null) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Không tìm thấy thông tin liên lạc hợp lệ"));
            } else {
                Order order = orderRepository.findByOrderID(orderNo);
                if (order != null) {
                    phoneNumber = order.getPhoneNumber();
                    address = order.getDeliveryAddress();
                }
            }
        }

        // If both phoneNumber and address are still null, return an appropriate message
        if (phoneNumber == null || address == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Không tìm thấy thông tin liên lạc hợp lệ"));
        }

        AccountContactInfoDTO contactInfo = new AccountContactInfoDTO(phoneNumber, address);
        return ResponseEntity.ok(contactInfo);
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
}
