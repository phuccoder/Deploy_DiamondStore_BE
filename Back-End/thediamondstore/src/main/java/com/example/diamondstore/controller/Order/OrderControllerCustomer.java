package com.example.diamondstore.controller.Order;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.diamondstore.model.Order;
import com.example.diamondstore.request.putRequest.OrderPutRequest;
import com.example.diamondstore.service.OrderService;

@RestController
@RequestMapping("/api/customer/order-management/orders")
public class OrderControllerCustomer {

    private final OrderService orderService;

    public OrderControllerCustomer(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/get-order/{orderID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Order> getOrderCustomer(@PathVariable Integer orderID) {
        Order order = orderService.getOrder(orderID);

        if (order == null) {
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.ok(order);
    }

    @GetMapping(value = "/get-account/{accountID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getOrdersByAccountCustomer(@PathVariable Integer accountID) {
        try {
            return ResponseEntity.ok(orderService.getOrdersByAccountId(accountID));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> createOrderCustomer(
            @RequestParam Integer accountID,
            @RequestParam String deliveryAddress,
            @RequestParam(required = false) String promotionCode,
            @RequestParam(required = false) Integer pointsToRedeem,
            @RequestParam String phoneNumber) {

        Order order = orderService.createOrder(accountID, deliveryAddress, promotionCode, pointsToRedeem, phoneNumber);
        return ResponseEntity.ok(Collections.singletonMap("message", "Tạo đơn hàng thành công"));
    }

    @DeleteMapping(value = "/cancel/{orderID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> cancelOrderCustomer(@PathVariable Integer orderID) {
        try {
            orderService.cancelOrder(orderID);
            return ResponseEntity.ok(Collections.singletonMap("message", "Hủy đơn hàng thành công"));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @PutMapping(value = "/update/{orderID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> updateOrder(@PathVariable Integer orderID, @RequestBody OrderPutRequest orderPutRequest) {
        ResponseEntity<?> response = orderService.updateOrder(orderID, orderPutRequest);
        return response;
    }

    @GetMapping(value = "/get-order/get-paging", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getOrdersPagedCustomer(@RequestParam Integer page, @RequestParam Integer size) {
        return ResponseEntity.ok(orderService.getAllOrdersPaged(page, size));
    }

    @GetMapping(value = "/get-by-status/{orderStatus}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getOrdersByStatusCustomer(@PathVariable String orderStatus) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(orderStatus));
    }
}
