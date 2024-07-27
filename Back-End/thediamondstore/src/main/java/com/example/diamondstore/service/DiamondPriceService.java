package com.example.diamondstore.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.diamondstore.model.Diamond;
import com.example.diamondstore.model.DiamondPrice;
import com.example.diamondstore.repository.DiamondPriceRepository;
import com.example.diamondstore.repository.DiamondRepository;
import com.example.diamondstore.request.DiamondPriceRequest;
import com.example.diamondstore.specification.DiamondPriceSpecification;

@Service
public class DiamondPriceService {

    @Autowired
    private final DiamondPriceRepository diamondPriceRepository;

    @Autowired
    private DiamondRepository diamondRepository;

    public DiamondPriceService(DiamondPriceRepository diamondPriceRepository) {
        this.diamondPriceRepository = diamondPriceRepository;
    }

    public List<DiamondPrice> getAll() {
        return diamondPriceRepository.findAll();
    }

    public List<DiamondPrice> findByCriteria(String clarity, String color, BigDecimal caratSize) {
        return diamondPriceRepository.findAll(DiamondPriceSpecification.filterBy(clarity, color, caratSize));
    }

    @Transactional
    public ResponseEntity<Map<String, String>> updateDiamondPrice(@PathVariable Integer diamondPriceID, @RequestBody DiamondPriceRequest diamondPriceRequest) {
        DiamondPrice existingDiamondPrice = diamondPriceRepository.findByDiamondPriceID(diamondPriceID);
        if (existingDiamondPrice == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Không tìm thấy giá kim cương để cập nhật"));
        }

        existingDiamondPrice.setClarity(diamondPriceRequest.getClarity());
        existingDiamondPrice.setColor(diamondPriceRequest.getColor());
        existingDiamondPrice.setCaratSize(diamondPriceRequest.getCaratSize());
        existingDiamondPrice.setDiamondEntryPrice(diamondPriceRequest.getDiamondEntryPrice());

        // size (mm) = sqrt(weight) * 6.5 => weight = (size / 6.5) ^ 2
        BigDecimal weight = new BigDecimal(Math.pow(diamondPriceRequest.getCaratSize().doubleValue() / 6.5, 2));
        existingDiamondPrice.setWeight(weight);

        if (diamondPriceRepository.existsByCaratSizeAndColorAndClarityAndDiamondPriceIDNot(diamondPriceRequest.getCaratSize(), diamondPriceRequest.getColor(),
                diamondPriceRequest.getClarity(), diamondPriceID)) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Đã tồn tại Giá cho loại kim cương này"));
        }

        diamondPriceRepository.save(existingDiamondPrice);

        List<Diamond> diamonds = diamondRepository.findAllByCaratSizeAndColorAndClarity(
                diamondPriceRequest.getCaratSize(), diamondPriceRequest.getColor(), diamondPriceRequest.getClarity());

        for (Diamond diamond : diamonds) {
            diamond.setDiamondEntryPrice(diamondPriceRequest.getDiamondEntryPrice());
            BigDecimal grossDiamondPrice = diamondPriceRequest.getDiamondEntryPrice().multiply(new BigDecimal("1.1"));
            diamond.setGrossDiamondPrice(grossDiamondPrice);
            diamond.setDiamondPrice(existingDiamondPrice);
            diamondRepository.save(diamond);
        }

        return ResponseEntity.ok(Collections.singletonMap("message", "Cập nhật thành công"));
    }

    public ResponseEntity<?> addDiamondPrice(DiamondPriceRequest diamondPriceRequest) {
        DiamondPrice diamondPrice = new DiamondPrice();
        diamondPrice.setClarity(diamondPriceRequest.getClarity());
        diamondPrice.setColor(diamondPriceRequest.getColor());
        diamondPrice.setCaratSize(diamondPriceRequest.getCaratSize());
        diamondPrice.setDiamondEntryPrice(diamondPriceRequest.getDiamondEntryPrice());

        if (diamondPriceRepository.existsByCaratSizeAndColorAndClarity(diamondPriceRequest.getCaratSize(), diamondPriceRequest.getColor(),
                diamondPriceRequest.getClarity())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Đã tồn tại Giá cho loại kim cương này"));
        }

        BigDecimal weight = new BigDecimal(Math.pow(diamondPriceRequest.getCaratSize().doubleValue() / 6.5, 2));
        diamondPrice.setWeight(weight);

        diamondPriceRepository.save(diamondPrice);

        List<Diamond> diamonds = diamondRepository.findAllByCaratSizeAndColorAndClarity(
                diamondPriceRequest.getCaratSize(), diamondPriceRequest.getColor(), diamondPriceRequest.getClarity());

        for (Diamond diamond : diamonds) {
            diamond.setDiamondEntryPrice(diamondPriceRequest.getDiamondEntryPrice());
            BigDecimal grossDiamondPrice = diamondPriceRequest.getDiamondEntryPrice().multiply(new BigDecimal("1.1"));
            diamond.setGrossDiamondPrice(grossDiamondPrice);
            diamond.setDiamondPrice(diamondPrice);
            diamondRepository.save(diamond);
        }

        return ResponseEntity.ok(Collections.singletonMap("message", "Tạo thành công"));
    }

    public DiamondPrice getDiamondPriceById(Integer diamondPriceID) {
        return diamondPriceRepository.findById(diamondPriceID).orElseThrow(() -> new RuntimeException("DiamondPrice not found"));
    }

    public ResponseEntity<Map<String, String>> deleteDiamondPrices(@RequestBody List<Integer> diamondPriceIDs) {
        List<Integer> existingDiamondPriceIDs = diamondPriceIDs.stream()
                .filter(diamondPriceID -> diamondPriceRepository.existsById(diamondPriceID))
                .collect(Collectors.toList());

        if (!existingDiamondPriceIDs.isEmpty()) {
            diamondPriceRepository.deleteAllById(existingDiamondPriceIDs);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "Xóa các giá kim cương thành công"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Không tìm thấy giá kim cương để xóa"));
        }
    }

    public List<DiamondPrice> getDiamondPricesByCaratSize(BigDecimal caratSize) {
        return diamondPriceRepository.findByCaratSize(caratSize);
    }

    public List<String> getClarity() {
        return diamondPriceRepository.findAll().stream()
                .map(DiamondPrice::getClarity)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<BigDecimal> getCaratSize() {
        return diamondPriceRepository.findAll().stream()
                .map(DiamondPrice::getCaratSize)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getColor() {
        return diamondPriceRepository.findAll().stream()
                .map(DiamondPrice::getColor)
                .distinct()
                .collect(Collectors.toList());
    }
}
