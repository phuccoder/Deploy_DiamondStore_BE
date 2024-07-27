package com.example.diamondstore.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.diamondstore.model.Promotion;
import com.example.diamondstore.request.PromotionRequest;
import com.example.diamondstore.request.putRequest.PromotionPutRequest;
import com.example.diamondstore.service.PromotionService;

@RestController
@RequestMapping("/api")
public class PromotionController {

    @Autowired
    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    // admin + manager
    @GetMapping(value = "/promotion-management/promotions/get-all", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Iterable<Promotion>> getAllPromotion() {
        return ResponseEntity.ok(promotionService.getAllPromotions());
    }

    // manager  
    @GetMapping(value = "/manager/promotion-management/promotions/{promotionID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Promotion> getPromotionByIDManager(@PathVariable Integer promotionID) {
        Promotion promotion = promotionService.getPromotionById(promotionID);
        if (promotion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(promotion);
    }

    // customer
    @GetMapping(value = "/customer/promotions/get-by-promotion-id/{promotionID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Promotion> getPromotionByIDCustomer(@PathVariable Integer promotionID) {
        Promotion promotion = promotionService.getPromotionById(promotionID);
        if (promotion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(promotion);
    }

    // manager
    @GetMapping(value = "/manager/promotion-management/promotions/{promotionCode}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Promotion> getPromotionByCodeManager(@PathVariable String promotionCode) {
        Promotion promotion = promotionService.getPromotionByCode(promotionCode);
        if (promotion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(promotion);
    }

    // customer
    @GetMapping(value = "/customer/promotions/get-by-promotion-code/{promotionCode}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Promotion> getPromotionByCodeCustomer(@PathVariable String promotionCode) {
        Promotion promotion = promotionService.getPromotionByCode(promotionCode);
        if (promotion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(promotion);
    }

    // admin
    @PutMapping(value = "/manager/promotion-management/promotions/update/{promotionID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> updatePromotionManager(@PathVariable Integer promotionID, @RequestBody PromotionPutRequest promotionPutRequest) {
        return promotionService.updatePromotion(promotionID, promotionPutRequest);
    }

    // admin
    @DeleteMapping(value = "/manager/promotion-management/promotions/delete", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> deletePromotionsManager(@RequestBody List<Integer> promotionIDs) {
        try {
            promotionService.deletePromotions(promotionIDs);
            return ResponseEntity.ok(Collections.singletonMap("message", "Xóa các tài khoản thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    // admin
    @PostMapping(value = "/manager/promotion-management/promotions/add", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> createPromotionManager(@RequestBody PromotionRequest promotionRequest) {
        return promotionService.createPromotion(promotionRequest);
    }

    // admin
    @PostMapping(value = "/manager/promotion-management/promotions/update-status", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> updatePromotionStatusesAutoManager() {
        promotionService.updatePromotionStatusesAuto();
        return ResponseEntity.ok(Collections.singletonMap("message", "Trạng thái khuyến mãi đã được cập nhật"));
    }
}
