package com.example.diamondstore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.diamondstore.model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findByAccountName(String accountName);

    Optional<Account> findByEmail(String email);

    Account findByAccountID(Integer accountID);

    List<Account> findByRole(String role);

    List<Account> findByRoleNot(String role);

}

