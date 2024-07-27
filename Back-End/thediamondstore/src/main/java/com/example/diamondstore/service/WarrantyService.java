package com.example.diamondstore.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.diamondstore.model.Diamond;
import com.example.diamondstore.model.Jewelry;
import com.example.diamondstore.model.Warranty;
import com.example.diamondstore.repository.DiamondRepository;
import com.example.diamondstore.repository.JewelryRepository;
import com.example.diamondstore.repository.WarrantyRepository;
import com.example.diamondstore.request.putRequest.WarrantyPutRequest;

@Service
public class WarrantyService {

    @Autowired
    private WarrantyRepository warrantyRepository;

    @Autowired
    private DiamondRepository diamondRepository;

    @Autowired
    private JewelryRepository jewelryRepository;

    public List<Warranty> getAllWarranties() {
        return warrantyRepository.findAll();
    }

    public Warranty getWarrantyById(String warrantyID) {
        return warrantyRepository.findByWarrantyID(warrantyID);
    }

    public Page<Warranty> getAllWarrantiesPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return warrantyRepository.findAll(pageable);
    }

    public ResponseEntity<Map<String, String>> createWarranty(Warranty warranty) {
        if (!validateWarrantyID(warranty.getWarrantyID())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Mã bảo hành không hợp lệ"));
        }

        if (warranty.getDiamondID() != null && warranty.getDiamondID().isEmpty()) {
            warranty.setDiamondID(null);
        }
        if (warranty.getJewelryID() != null && warranty.getJewelryID().isEmpty()) {
            warranty.setJewelryID(null);
        }

        Warranty existingWarranty = warrantyRepository.findByWarrantyID(warranty.getWarrantyID());
        if (existingWarranty != null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Giấy bảo hành đã tồn tại"));
        }

        if (warranty.getDiamondID() == null && warranty.getJewelryID() == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Cần cung cấp ít nhất một ID cho kim cương hoặc trang sức"));
        }

        if (warranty.getDiamondID() != null && warranty.getJewelryID() != null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Chỉ có thể có một trong hai ID cho kim cương hoặc trang sức"));
        }

    warrantyRepository.save(warranty);

    Diamond diamond = diamondRepository.findByDiamondID(warranty.getDiamondID());
    Jewelry jewelry = jewelryRepository.findByJewelryID(warranty.getJewelryID());

    if (diamond != null) {
        diamond.setWarrantyID(warranty.getWarrantyID());
        diamondRepository.save(diamond);
    } else if (jewelry != null) {
        jewelry.setWarrantyID(warranty.getWarrantyID());
        jewelryRepository.save(jewelry);
    } else {
        return ResponseEntity.badRequest().body(Collections.singletonMap("message", "ID không tồn tại"));
    }

    return ResponseEntity.ok(Collections.singletonMap("message", "Giấy bảo hành đã được tạo thành công"));
    }

    public ResponseEntity<Map<String, String>> updateWarranty(String warrantyID, WarrantyPutRequest warrantyPutRequest) {
        if (!validateWarrantyID(warrantyID)) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Mã giấy bảo hành không hợp lệ"));
        }

        Warranty existingWarranty = warrantyRepository.findByWarrantyID(warrantyID);
        if (existingWarranty == null) {
            return ResponseEntity.notFound().build();
        }
        existingWarranty.setDiamondID(warrantyPutRequest.getDiamondID());
        existingWarranty.setWarrantyImage(warrantyPutRequest.getWarrantyImage());
        warrantyRepository.save(existingWarranty);
        return ResponseEntity.ok(Collections.singletonMap("message", "Giấy bảo hành đã được cập nhật thành công"));
    }

    @Transactional
    public ResponseEntity<Map<String, String>> deleteWarranty(@RequestBody List<String> warrantyIDs) {
        if (warrantyIDs.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Không có mã giấy bảo hành để xóa"));
        }

        for (String warrantyID : warrantyIDs) {
            if (!validateWarrantyID(warrantyID)) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Mã giấy bảo hành không hợp lệ"));
            }
        }

        List<String> existingWarrantyIDs = warrantyIDs.stream()
                .filter(warrantyID -> warrantyRepository.existsById(warrantyID))
                .collect(Collectors.toList());

        if (existingWarrantyIDs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Không tìm thấy giá vàng để xóa"));
        }

        List<Diamond> diamondsToUpdate = diamondRepository.findAllByWarrantyIDIn(existingWarrantyIDs);
        for (Diamond diamond : diamondsToUpdate) {
            diamond.setWarrantyID(null);
        }
        diamondRepository.saveAll(diamondsToUpdate);

        List<Jewelry> jewelryToUpdate = jewelryRepository.findAllByWarrantyIDIn(existingWarrantyIDs);
        for (Jewelry jewelry : jewelryToUpdate) {
            jewelry.setWarrantyID(null);
        }
        jewelryRepository.saveAll(jewelryToUpdate);

        warrantyRepository.deleteAllById(existingWarrantyIDs);

        return ResponseEntity.ok().body(Collections.singletonMap("message", "Xóa các giá vàng thành công"));
    }

    public ResponseEntity<Map<String, String>> getWarrantyImg(String warrantyID) {
        if (!validateWarrantyID(warrantyID)) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Mã bảo hành không hợp lệ"));
        }

        Warranty warranty = warrantyRepository.findByWarrantyID(warrantyID);
        if (warranty == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "ID không tồn tại"));
        }
        return ResponseEntity.ok(Collections.singletonMap("warrantyImage", warranty.getWarrantyImage()));
    }

    public List<Warranty> getWarrantiesByDiamondIDIsNull() {
        return warrantyRepository.findByDiamondIDIsNull();
    }

    public List<Warranty> getWarrantiesByJewelryIDIsNull() {
        return warrantyRepository.findByJewelryIDIsNull();
    }

    public boolean validateWarrantyID(String warrantyID) {
        if (!warrantyID.startsWith("WID-")) {
            return false;
        }
        return true;
    }
}
