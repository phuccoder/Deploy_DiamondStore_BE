package com.example.diamondstore.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.diamondstore.model.Promotion;
import com.example.diamondstore.repository.PromotionRepository;
import com.example.diamondstore.request.PromotionRequest;
import com.example.diamondstore.request.putRequest.PromotionPutRequest;

@Service
public class PromotionService {

    @Autowired
    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    public Promotion getPromotionById(Integer promotionID) {
        return promotionRepository.findByPromotionID(promotionID);
    }

    public Promotion getPromotionByCode(String promotionCode) {
        return promotionRepository.findByPromotionCode(promotionCode);
    }

    public ResponseEntity<Map<String, String>> createPromotion(PromotionRequest promotionRequest) {
        Promotion existingPromotion = promotionRepository.findByPromotionCode(promotionRequest.getPromotionCode());
        if (existingPromotion != null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Khuyến mãi đã tồn tại"));
        }
        Promotion newPromotion = new Promotion();
        newPromotion.setPromotionCode(promotionRequest.getPromotionCode());
        newPromotion.setStartDate(promotionRequest.getStartDate());
        newPromotion.setEndDate(promotionRequest.getEndDate());
        newPromotion.setDiscountAmount(promotionRequest.getDiscountAmount());
        newPromotion.setDescription(promotionRequest.getDescription());
        updatePromotionStatus(newPromotion); // Cập nhật trạng thái trước khi lưu
        promotionRepository.save(newPromotion);
        return ResponseEntity.ok(Collections.singletonMap("message", "Tạo thành công"));
    }

    public ResponseEntity<Map<String, String>> updatePromotion(Integer promotionID, PromotionPutRequest promotionPutRequest) {
        Promotion existingPromotion = promotionRepository.findByPromotionID(promotionID);
        if (existingPromotion == null) {
            return ResponseEntity.notFound().build();
        }
        existingPromotion.setPromotionCode(promotionPutRequest.getPromotionCode());
        existingPromotion.setStartDate(promotionPutRequest.getStartDate());
        existingPromotion.setEndDate(promotionPutRequest.getEndDate());
        existingPromotion.setDiscountAmount(promotionPutRequest.getDiscountAmount());
        existingPromotion.setDescription(promotionPutRequest.getDescription());
        updatePromotionStatus(existingPromotion);
        promotionRepository.save(existingPromotion);
        return ResponseEntity.ok(Collections.singletonMap("message", "Cập nhật thành công"));
    }

    public ResponseEntity<Map<String, String>> deletePromotions(@RequestBody List<Integer> promotionIDs) {
        // Filter out non-existing diamonds
        List<Integer> existingPromotionIDs = promotionIDs.stream()
                .filter(promotionID -> promotionRepository.existsById(promotionID))
                .collect(Collectors.toList());

        // Delete diamonds
        if (!existingPromotionIDs.isEmpty()) {
            promotionRepository.deleteAllById(existingPromotionIDs);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "Xóa các giá vàng thành công"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Không tìm thấy giá vàng để xóa"));
        }
    }

    @PostConstruct
    public void updatePromotionStatusesOnStartup() {
        updatePromotionStatusesAuto();
    }

    @Scheduled(cron = "0 0 * * * *") // Chạy mỗi giờ
    public void updatePromotionStatusesAuto() {
        List<Promotion> promotions = promotionRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Promotion promotion : promotions) {
            if (promotion.getEndDate().isBefore(now)) {
                promotion.setPromotionStatus("Hết Hạn");
            } else {
                promotion.setPromotionStatus("Còn Hạn");
            }
            promotionRepository.save(promotion);
        }
    }

     private void updatePromotionStatus(Promotion promotion) {
        LocalDateTime now = LocalDateTime.now();
        if (promotion.getEndDate().isBefore(now)) {
            promotion.setPromotionStatus("Hết Hạn");
        } else {
            promotion.setPromotionStatus("Còn Hạn");
        }
    }

}