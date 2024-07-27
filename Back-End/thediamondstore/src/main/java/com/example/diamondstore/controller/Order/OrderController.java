package com.example.diamondstore.controller.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.diamondstore.request.putRequest.OrderPutRequest;
import com.example.diamondstore.service.OrderService;

@RestController
@RequestMapping("/api/order-management/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping(value = "/get-all", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping(value = "/get-order-have-transaction-no", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getOrdersHaveTransactionNo() {
        return ResponseEntity.ok(orderService.getOrdersHaveTransactionNo());
    }

    @GetMapping(value = "/get-order/get-paging", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getOrdersPaged(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(orderService.getAllOrdersPaged(page, size));
    }

    @PutMapping(value = "/update/{orderID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> updateOrderManager(@PathVariable Integer orderID, @RequestBody OrderPutRequest orderPutRequest) {
        ResponseEntity<?> response = orderService.updateOrder(orderID, orderPutRequest);
        return response;
    }
}
