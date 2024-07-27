package com.example.diamondstore.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.diamondstore.model.GoldPrice;
import com.example.diamondstore.model.Jewelry;
import com.example.diamondstore.repository.GoldPriceRepository;
import com.example.diamondstore.repository.JewelryRepository;
import com.example.diamondstore.request.GoldPriceRequest;

@Service
public class GoldPriceService {
    
    @Autowired
    private final GoldPriceRepository goldPriceRepository;

    @Autowired
    private final JewelryRepository jewelryRepository;

    public GoldPriceService(GoldPriceRepository goldPriceRepository, JewelryRepository jewelryRepository) {
        this.goldPriceRepository = goldPriceRepository;
        this.jewelryRepository = jewelryRepository;
    }

    public List<GoldPrice> getAll() {
        return goldPriceRepository.findAll();
    }

    public GoldPrice getGoldPriceById(Integer goldPriceID) {
        return goldPriceRepository.findById(goldPriceID).orElse(null);
    }

    public ResponseEntity<Map<String, String>> deleteGoldPrices(@RequestBody List<Integer> goldPriceIDs) {
        // Filter out non-existing diamonds
        List<Integer> existingGoldPriceIDs = goldPriceIDs.stream()
                .filter(goldPriceID -> goldPriceRepository.existsById(goldPriceID))
                .collect(Collectors.toList());

        // Delete diamonds
        if (!existingGoldPriceIDs.isEmpty()) {
            goldPriceRepository.deleteAllById(existingGoldPriceIDs);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "Xóa các giá vàng thành công"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Không tìm thấy giá vàng để xóa"));
        }
    }

    public ResponseEntity<?> addGoldPrice(GoldPriceRequest goldPriceRequest) {
        Jewelry jewelry = jewelryRepository.findById(goldPriceRequest.getJewelryID()).orElse(null);
        if (jewelry == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Không tìm thấy trang sức với ID này"));
        }

        GoldPrice existingGoldPrice = goldPriceRepository.findByJewelryID(goldPriceRequest.getJewelryID());
        if (existingGoldPrice != null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Giá vàng cho trang sức này đã tồn tại"));
        }
        GoldPrice goldPrice = new GoldPrice();
        goldPrice.setGoldPrice(goldPriceRequest.getGoldPrice());
        goldPrice.setJewelryID(goldPriceRequest.getJewelryID());
        goldPrice.setGoldAge(goldPriceRequest.getGoldAge());
        
        goldPriceRepository.save(goldPrice);

        updateJewelryPrices(goldPrice, jewelry);

        return ResponseEntity.ok(Collections.singletonMap("message", "Tạo thành công"));
    }

    public ResponseEntity<?> updateGoldPrice(Integer goldPriceID, GoldPriceRequest goldPriceRequest) {
        GoldPrice existingGoldPrice = goldPriceRepository.findById(goldPriceID).orElse(null);
        if (existingGoldPrice == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Không tìm thấy giá vàng"));
        }

        GoldPrice goldPriceWithNewJewelryID = goldPriceRepository.findByJewelryID(goldPriceRequest.getJewelryID());
        if (goldPriceWithNewJewelryID != null && !goldPriceWithNewJewelryID.getGoldpriceID().equals(goldPriceID)) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Giá vàng này đã tồn tại"));
        }
        existingGoldPrice.setGoldPrice(goldPriceRequest.getGoldPrice());
        existingGoldPrice.setGoldAge(goldPriceRequest.getGoldAge());
        existingGoldPrice.setJewelryID(goldPriceRequest.getJewelryID());
        
        goldPriceRepository.save(existingGoldPrice);

        Jewelry jewelry = jewelryRepository.findById(goldPriceRequest.getJewelryID()).orElse(null);
        if (jewelry != null) {
            updateJewelryPrices(existingGoldPrice, jewelry);
        }

        return ResponseEntity.ok(Collections.singletonMap("message", "Cập nhật thành công"));
    }

    private void updateJewelryPrices(GoldPrice goldPrice, Jewelry jewelry) {
        jewelry.setJewelryEntryPrice(goldPrice.getGoldPrice());

        // Assume grossJewelryPrice is calculated based on some logic involving goldPrice
        BigDecimal grossJewelryPrice = goldPrice.getGoldPrice().multiply(new BigDecimal("1.2")); // Example logic
        jewelry.setGrossJewelryPrice(grossJewelryPrice);

        jewelryRepository.save(jewelry);
    }
}
