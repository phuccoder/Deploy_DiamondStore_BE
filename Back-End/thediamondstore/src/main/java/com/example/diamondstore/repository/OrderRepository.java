package com.example.diamondstore.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.diamondstore.model.Account;
import com.example.diamondstore.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    Order findByOrderID(int orderID);

    List<Order> findByAccount(Account account);

    Order getByOrderID(int orderID);

    List<Order> findByOrderStatus(String orderStatus);

    List<Order> findByTransactionNoNotNull();

    List<Order> findAllByOrderStatus(String orderStatus);

    Long countByOrderStatus(String orderStatus);

    Integer countByStartorderDateBetween(LocalDateTime start, LocalDateTime end);

    List<Order> findAllByStartorderDateBetween(LocalDateTime start, LocalDateTime end);

    Account deleteByAccount(Account account);

    void deleteByAccount_AccountID(Integer accountID);

    List<Order> findByAccount_AccountID(Integer accountID);
    
}
