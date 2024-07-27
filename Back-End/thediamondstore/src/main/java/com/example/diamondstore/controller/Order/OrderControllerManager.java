package com.example.diamondstore.controller.Order;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.diamondstore.DTO.OrderSummaryDTO;
import com.example.diamondstore.request.putRequest.OrderPutRequest;
import com.example.diamondstore.service.OrderService;

@RestController
@RequestMapping("/api/manager/order-management/orders")
public class OrderControllerManager {

    private final OrderService orderService;

    public OrderControllerManager(OrderService orderService) {
        this.orderService = orderService;
    }

    // api get totalOrder by orderID
    @GetMapping(value = "/total-order/{orderID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getTotalOrder(@PathVariable Integer orderID) {
        try {
            return ResponseEntity.ok(orderService.getTotalOrder(orderID));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @GetMapping(value = "/get-by-status/{orderStatus}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getOrdersByStatus(@PathVariable String orderStatus) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(orderStatus));
    }

    @GetMapping(value = "/total-revenue", produces = "application/json;charset=UTF-8")
    public ResponseEntity<BigDecimal> getTotalOrderPaid() {
        BigDecimal totalOrderPaid = orderService.getTotalOrderByOrderStatusPaid();
        return ResponseEntity.ok(totalOrderPaid);
    }

    @PutMapping(value = "/update/{orderID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> updateOrderManager(@PathVariable Integer orderID, @RequestBody OrderPutRequest orderPutRequest) {
        ResponseEntity<?> response = orderService.updateOrder(orderID, orderPutRequest);
        return response;
    }

    @GetMapping(value = "/total-order", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Long> getTotalOrder() {
        long totalOrder = orderService.getTotalOrders();
        return new ResponseEntity<>(totalOrder, HttpStatus.OK);
    }

    @GetMapping(value = "/total-transaction", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Long> getTotalOrderStatusPaid() {
        long totalOrderStatusPaid = orderService.getTotalOrdersByOrderStatus("Đã thanh toán");
        return new ResponseEntity<>(totalOrderStatusPaid, HttpStatus.OK);
    }

    // API to get total number of orders in a day
    @GetMapping(value = "/total-orders-in-day", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Integer> getTotalOrdersInDay() {
        int totalOrders = orderService.getTotalOrdersInDay();
        return new ResponseEntity<>(totalOrders, HttpStatus.OK);
    }

    // API to get total order revenue in a day
    @GetMapping(value = "/total-revenue-value-in-today", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getRevenueValueInToday() {
        OrderSummaryDTO orderSummary = orderService.getRevenueValueInToday();
        return new ResponseEntity<>(orderSummary, HttpStatus.OK);
    }

    // API to get total order value in a month
    @GetMapping(value = "/total-revenue-day-in-month", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getRevenueDayInMonth() {
        List<OrderSummaryDTO> orderSummaries = orderService.getRevenueDayInMonth();
        return new ResponseEntity<>(orderSummaries, HttpStatus.OK);
    }

    @GetMapping(value = "/total-revenue-day-in-month-in-year", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getRevenueDayInMonthInYear(@RequestParam int month, @RequestParam int year) {
        List<OrderSummaryDTO> orderSummaries = orderService.getRevenueDayInMonthInYear(month, year);
        return new ResponseEntity<>(orderSummaries, HttpStatus.OK);
    }

    // API to get total order value in a year
    @GetMapping(value = "/total-revenue-month-in-year", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getRevenueMonthInYear() {
        List<OrderSummaryDTO> orderSummaries = orderService.getRevenueMonthInYear();
        return new ResponseEntity<>(orderSummaries, HttpStatus.OK);
    }

    @GetMapping(value = "/total-revenue-month-in-a-year", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getRevenueMonthInAYear(@RequestParam int year) {
        List<OrderSummaryDTO> orderSummaries = orderService.getRevenueMonthInYear(year);
        return new ResponseEntity<>(orderSummaries, HttpStatus.OK);
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
}
