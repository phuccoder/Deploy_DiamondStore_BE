package com.example.diamondstore.controller.GoldPrice;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.diamondstore.request.GoldPriceRequest;
import com.example.diamondstore.service.GoldPriceService;

@RestController
@RequestMapping("/api/manager/gold-price-management/gold-prices")
public class GoldPriceControllerManager {

    private final GoldPriceService goldPriceService;

    public GoldPriceControllerManager(GoldPriceService goldPriceService) {
        this.goldPriceService = goldPriceService;
    }

    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> addGoldPrice(@RequestBody GoldPriceRequest goldPriceRequest) {
        return goldPriceService.addGoldPrice(goldPriceRequest);
    }

    @PutMapping(value = "/update/{goldPriceID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> updateGoldPrice(@PathVariable Integer goldPriceID, @RequestBody GoldPriceRequest goldPriceRequest) {
        return goldPriceService.updateGoldPrice(goldPriceID, goldPriceRequest);
    }

    @DeleteMapping(value = "/delete", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> deleteGoldPrices(@RequestBody List<Integer> goldPriceIDs) {
        try {
            goldPriceService.deleteGoldPrices(goldPriceIDs);
            return ResponseEntity.ok(Collections.singletonMap("message", "Xóa các tài khoản thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }
}
