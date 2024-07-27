package com.example.diamondstore.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.diamondstore.model.OrderDetail;
import com.example.diamondstore.repository.OrderDetailRepository;

@Service
public class OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> getOrderDetailByOrderID(Integer orderID) {
        return orderDetailRepository.findByOrder_OrderID(orderID);
    }

    public OrderDetail getOrderDetail(Integer orderDetailID) {
        return orderDetailRepository.findById(orderDetailID).orElse(null);
    }

    public List<OrderDetail> getAllOrderDetail() {
        return orderDetailRepository.findAll();
    }
}
