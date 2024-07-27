package com.example.diamondstore.controller.DiamondPrice;

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

import com.example.diamondstore.request.DiamondPriceRequest;
import com.example.diamondstore.service.DiamondPriceService;

@RestController
@RequestMapping("/api/manager/diamond-price-management/diamond-prices")
public class DiamondPriceControllerManager {

    private final DiamondPriceService diamondPriceService;

    public DiamondPriceControllerManager(DiamondPriceService diamondPriceService) {
        this.diamondPriceService = diamondPriceService;
    }

    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> addDiamondPrice(@RequestBody DiamondPriceRequest diamondPriceRequest) {
        return diamondPriceService.addDiamondPrice(diamondPriceRequest);
    }

    @PutMapping(value = "/update/{diamondPriceID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> updateDiamondPrice(@PathVariable Integer diamondPriceID, @RequestBody DiamondPriceRequest diamondPriceRequest) {
        return diamondPriceService.updateDiamondPrice(diamondPriceID, diamondPriceRequest);
    }

    @DeleteMapping(value = "/delete", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> deleteDiamondPrices(@RequestBody List<Integer> diamondPriceIDs) {
        try {
            diamondPriceService.deleteDiamondPrices(diamondPriceIDs);
            return ResponseEntity.ok(Collections.singletonMap("message", "Xóa các tài khoản thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }
}
