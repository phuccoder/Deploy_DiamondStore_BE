package com.example.diamondstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.diamondstore.model.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    List<OrderDetail> findByOrder_OrderID(Integer orderID);

    List<OrderDetail> findByAccount_AccountID(Integer accountID);

    void deleteByAccount_AccountID(Integer accountID);
}
