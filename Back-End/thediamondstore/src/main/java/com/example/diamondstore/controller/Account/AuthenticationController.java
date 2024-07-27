package com.example.diamondstore.controller.Account;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.diamondstore.request.AuthenticationRequest;
import com.example.diamondstore.request.RegisterRequest;
import com.example.diamondstore.service.AccountService;
import com.example.diamondstore.service.AuthenticationService;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private  AccountService accountService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/api/auth/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        return authenticationService.authenticate(authenticationRequest);
    }

    @PostMapping(value = "/api/auth/refresh-token", produces = "application/json")
    public ResponseEntity<?> refreshAuthenticationToken(@RequestHeader("Authorization") String token) {
        return authenticationService.refresh(token.substring(0));
    }

    @PostMapping(value = "/api/auth/register", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest registerRequest) {
        Map<String, String> message;
        try {
            message = accountService.register(registerRequest);
            return ResponseEntity.ok().body(Map.of("message", message.get("message")));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping(value = "/api/auth/regenerate-otp", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> regenerateOtp(@RequestParam String email) {
        Map<String, String> response;
        try {
            response = accountService.regenerateOtp(email);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping(value = "/api/auth/set-password", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> setPassword(@RequestParam String email, @RequestHeader String newPassword) {
        try {
            Map<String, String> response = accountService.setPassword(email, newPassword);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

     @PutMapping(value = "/api/auth/verify-account", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> verifyAccount(@RequestParam String email, @RequestParam String otp) {
        Map<String, String> response;
        try {
            response = accountService.verifyAccount(email, otp);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping(value = "/api/auth/forget-password", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> forgetPassword(@RequestParam String email) {
        try {
            Map<String, String> response = accountService.forgetPassword(email);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
