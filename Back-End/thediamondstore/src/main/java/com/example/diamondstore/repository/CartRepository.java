package com.example.diamondstore.repository;

import com.example.diamondstore.model.Cart;
import com.example.diamondstore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    Cart findByCartID(Integer cartID);

    List<Cart> findByAccount_AccountIDAndOrderIsNull(Integer accountID);

    Cart findByAccount_AccountIDAndDiamond_DiamondID(Integer accountID, String diamondID);

    Cart findByAccount_AccountIDAndJewelry_JewelryID(Integer accountID, String jewelryID);

    List<Cart> findByOrder(Order order);

    void deleteByAccount_AccountID(Integer accountID);

    void deleteByOrder_OrderID(Integer orderID);

    List<Cart> findByAccount_AccountID(Integer accountID);
}