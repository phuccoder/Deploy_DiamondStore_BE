package com.example.diamondstore.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.diamondstore.service.OrderDetailService;

@RestController
@RequestMapping("/api")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

    // admin + manager
    @GetMapping(value = "/orderDetail-management/orderDetails/get-all", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getAllOrderDetail() {
        return ResponseEntity.ok(orderDetailService.getAllOrderDetail());
    }

    // customer
    @GetMapping(value = "/customer/orderDetails/get-all", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getAllOrderDetailCustomer() {
        return ResponseEntity.ok(orderDetailService.getAllOrderDetail());
    }

    // admin + manager
    @GetMapping(value = "/orderDetail-management/orderDetails/get-orderDetail/{orderID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getOrderDetailByOrderID(@PathVariable Integer orderID) {
        return ResponseEntity.ok(orderDetailService.getOrderDetailByOrderID(orderID));
    }

    // customer
    @GetMapping(value = "/customer/orderDetails/get-orderDetail/{orderID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getOrderDetailByOrderIDCustomer(@PathVariable Integer orderID) {
        return ResponseEntity.ok(orderDetailService.getOrderDetailByOrderID(orderID));
    }

    // admin + manager
    @GetMapping(value = "/orderDetail-management/orderDetails/{orderDetailID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getOrderDetail(@PathVariable Integer orderDetailID) {
        return ResponseEntity.ok(orderDetailService.getOrderDetail(orderDetailID));
    }

    // customer
    @GetMapping(value = "/customer/orderDetails/{orderDetailID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getOrderDetailCustomer(@PathVariable Integer orderDetailID) {
        return ResponseEntity.ok(orderDetailService.getOrderDetail(orderDetailID));
    }
}
