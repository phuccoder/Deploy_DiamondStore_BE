package com.example.diamondstore.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.diamondstore.model.Account;
import com.example.diamondstore.model.AccumulatePoints;
import com.example.diamondstore.model.Order;
import com.example.diamondstore.repository.AccountRepository;
import com.example.diamondstore.repository.AccumulatePointsRepository;
import com.example.diamondstore.repository.CartRepository;
import com.example.diamondstore.repository.OrderRepository;
import com.example.diamondstore.request.AccountRequest;
import com.example.diamondstore.request.RegisterRequest;
import com.example.diamondstore.utils.EmailUtil;
import com.example.diamondstore.utils.JwtUtil;
import com.example.diamondstore.utils.OtpUtil;

import io.jsonwebtoken.Claims;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccumulatePointsRepository accumulatePointsRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OtpUtil otpUtil;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> accountOptional = accountRepository.findByEmail(email);
        Account account = accountOptional.orElseThrow(()
                -> new UsernameNotFoundException("Không tìm thấy tài khoản với email: " + email)
        );

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(account.getRole()));
        return new org.springframework.security.core.userdetails.User(
                account.getEmail(),
                account.getPassword(),
                authorities
        );
    }

    public List<Account> getAllAccountsExcludingRoleCustomer() {
        return accountRepository.findByRoleNot("ROLE_CUSTOMER");
    }

    public Map<String, String> register(RegisterRequest registerRequest) {
        String accountName = registerRequest.getAccountName();
        String password = registerRequest.getPassword();
        String email = registerRequest.getEmail();

        if (!hasText(accountName) || !hasText(password) || !hasText(email)) {
            throw new RuntimeException("Vui lòng nhập đầy đủ thông tin");
        }

        if (!email.matches("^[a-zA-Z0-9._%+-]+@gmail.com$") && !email.matches("^[a-zA-Z0-9._%+-]+@fpt.edu.vn$")) {
            throw new RuntimeException("Email không hợp lệ");
        }

        Optional<Account> existingAccount = accountRepository.findByEmail(email);
        if (existingAccount.isPresent()) {
            throw new RuntimeException("Tài khoản đã tồn tại");
        }

        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi OTP. Vui lòng thử lại.");
        }

        Account account = new Account(null, accountName, password, "ROLE_CUSTOMER", null, email, null, otp, LocalDateTime.now(), false);
        accountRepository.save(account);

        AccumulatePoints accumulatePoints = new AccumulatePoints(account.getAccountID(), 0);
        accumulatePointsRepository.save(accumulatePoints);

        return Collections.singletonMap("message", "Đăng ký thành công. Vui lòng kiểm tra email để xác nhận tài khoản.");
    }

    private boolean hasText(String text) {
        return text != null && !text.trim().isEmpty();
    }

    public Map<String, String> verifyAccount(String email, String otp) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại" + email));
        LocalDateTime otpGeneratedTime = account.getOtpGeneratedTime();
        LocalDateTime now = LocalDateTime.now();
        long otpAgeInSeconds = Duration.between(otpGeneratedTime, now).getSeconds();

        String otpAccount = account.getOtp().trim();

        if (otpAccount.equals(otp) && otpAgeInSeconds < 600) {
            account.setActive(true);
            accountRepository.save(account);
            return Collections.singletonMap("message", "Xác thực thành công");
        } else {
            return Collections.singletonMap("message", "Xác thực thất bại. Vui lòng thử lại.");
        }
    }

    public Map<String, String> regenerateOtp(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại" + email));
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi OTP. Vui lòng thử lại.");
        }
        account.setOtp(otp);
        account.setOtpGeneratedTime(LocalDateTime.now());
        accountRepository.save(account);
        return Collections.singletonMap("message", "Email xác thực đã được gửi. Vui lòng kiểm tra email.");
    }

    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email).orElse(null);
    }

    public void createAccount(AccountRequest accountRequest) {
        validateAccountRequest(accountRequest);

        String accountName = accountRequest.getAccountName();
        String password = accountRequest.getPassword();
        String role = accountRequest.getRole();
        String phoneNumber = accountRequest.getPhoneNumber();
        String email = accountRequest.getEmail();

        Optional<Account> existingAccount = accountRepository.findByEmail(email);
        if (existingAccount.isPresent()) {
            throw new RuntimeException("Tài khoản đã tồn tại");
        }

        Account account = new Account();
        account.setAccountName(accountName);
        account.setPassword(password);

        account.setRole(role);
        account.setPhoneNumber(phoneNumber);
        account.setEmail(email);
        account.setActive(true);

        accountRepository.save(account);
    }

    private void validateAccountRequest(AccountRequest accountRequest) {
        String accountName = accountRequest.getAccountName();
        String password = accountRequest.getPassword();
        String role = accountRequest.getRole();
        String phoneNumber = accountRequest.getPhoneNumber();
        String email = accountRequest.getEmail();

        if (!email.matches("^[a-zA-Z0-9._%+-]+@gmail.com$")) {
            throw new RuntimeException("Email không hợp lệ");
        }

        if (!phoneNumber.matches("^(090|093|089|096|097|098)[0-9]{7}$")) {
            throw new RuntimeException("Số điện thoại không hợp lệ");
        }

        if (!StringUtils.hasText(accountName) || !StringUtils.hasText(password) || !StringUtils.hasText(role) || !StringUtils.hasText(email)) {
            throw new RuntimeException("Vui lòng nhập đầy đủ thông tin");
        }
    }

    public Map<String, String> forgetPassword(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại" + email));
        try {
            emailUtil.sendSetPasswordEmail(email);
        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi email. Vui lòng thử lại.");
        }
        return Collections.singletonMap("message", "Email đã được gửi. Vui lòng kiểm tra email để thiết lập mật khẩu.");
    }

    public Map<String, String> setPassword(String email, String newPassword) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại" + email));
        account.setPassword(newPassword);
        accountRepository.save(account);
        return Collections.singletonMap("message", "Mật khẩu đã được thiết lập. Vui lòng đăng nhập.");
    }

    @Transactional
    public ResponseEntity<Map<String, String>> deleteAccounts(List<Integer> accountIDs) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(role -> role.equals("ROLE_ADMIN") || role.equals("ROLE_MANAGER"))
                .findFirst()
                .orElseThrow();

        List<Integer> existingAccountIDs = accountIDs.stream()
                .filter(accountID -> accountRepository.existsById(accountID))
                .filter(accountID -> {
                    Account account = accountRepository.findById(accountID).orElse(null);
                    return account != null
                            && !account.getRole().equals("ROLE_ADMIN");
                })
                .collect(Collectors.toList());

        if (existingAccountIDs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("message", "Không thể xóa tài khoản đang đăng nhập hoặc các tài khoản với vai trò ADMIN"));
        }

        existingAccountIDs.forEach(accountID -> {
        Account account = accountRepository.findById(accountID).orElseThrow();
        List<Order> orders = orderRepository.findByAccount_AccountID(accountID);
        orders.forEach((var order) -> {
            cartRepository.deleteByOrder_OrderID(order.getOrderID());
            orderRepository.delete(order);
        });
        accumulatePointsRepository.deleteByAccountID(accountID);
        accountRepository.delete(account);
    });

        return ResponseEntity.ok().body(Collections.singletonMap("message", "Xóa các tài khoản thành công"));
    }

    public Account updateAccount_Customer(Integer accountID, AccountRequest accountRequest) {
        Account existingAccount = accountRepository.findById(accountID).orElse(null);
        if (existingAccount == null) {
            throw new RuntimeException("Không tìm thấy tài khoản");
        }
        existingAccount.setAccountName(accountRequest.getAccountName());
        existingAccount.setEmail(accountRequest.getEmail());
        existingAccount.setPhoneNumber(accountRequest.getPhoneNumber());
        existingAccount.setRole(accountRequest.getRole());
        existingAccount.setAddressAccount(accountRequest.getAddressAccount());

        if (accountRequest.getPassword() != null && !accountRequest.getPassword().isEmpty()
                && !accountRequest.getPassword().equals(existingAccount.getPassword())) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(accountRequest.getPassword());
            existingAccount.setPassword(encodedPassword);
        }
        return accountRepository.save(existingAccount);
    }

    public Account updateAccountProfile(Integer accountID, AccountRequest accountRequest, String token) {
        Account existingAccount = accountRepository.findById(accountID).orElse(null);
        if (existingAccount == null) {
            throw new RuntimeException("Không tìm thấy tài khoản");
        }

        Claims claims = jwtUtil.extractAllClaims(token);
        String currentRole = claims.get("role", String.class);

        if (accountID.equals(claims.get("accountID")) && !accountRequest.getRole().equals(currentRole)) {
            throw new RuntimeException("Không thể cập nhật vai trò của tài khoản đang đăng nhập");
        }

        existingAccount.setAccountName(accountRequest.getAccountName());
        existingAccount.setEmail(accountRequest.getEmail());
        existingAccount.setPhoneNumber(accountRequest.getPhoneNumber());
        existingAccount.setRole(accountRequest.getRole());
        existingAccount.setAddressAccount(accountRequest.getAddressAccount());

        if (accountRequest.getPassword() != null && !accountRequest.getPassword().isEmpty()
                && !accountRequest.getPassword().equals(existingAccount.getPassword())) {
            existingAccount.setPassword(accountRequest.getPassword());
        }

        return accountRepository.save(existingAccount);
    }

    public Account updateAccount_Admin(Integer accountID, AccountRequest accountRequest) {
        Account existingAccount = accountRepository.findById(accountID).orElse(null);
        if (existingAccount == null) {
            throw new RuntimeException("Không tìm thấy tài khoản");
        }
        existingAccount.setAccountName(accountRequest.getAccountName());
        existingAccount.setEmail(accountRequest.getEmail());
        existingAccount.setPhoneNumber(accountRequest.getPhoneNumber());
        existingAccount.setRole(accountRequest.getRole());
        existingAccount.setAddressAccount(accountRequest.getAddressAccount());
        existingAccount.setActive(accountRequest.getActive());

        if (accountRequest.getPassword() != null && !accountRequest.getPassword().isEmpty()
                && !accountRequest.getPassword().equals(existingAccount.getPassword())) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(accountRequest.getPassword());
            existingAccount.setPassword(encodedPassword);
        }
        return accountRepository.save(existingAccount);
    }
}
