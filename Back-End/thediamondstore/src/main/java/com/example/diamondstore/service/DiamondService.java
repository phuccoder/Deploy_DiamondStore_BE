package com.example.diamondstore.service;

import java.math.BigDecimal;
import java.math.MathContext;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.diamondstore.model.Certificate;
import com.example.diamondstore.model.Diamond;
import com.example.diamondstore.model.DiamondPrice;
import com.example.diamondstore.repository.CertificateRepository;
import com.example.diamondstore.repository.DiamondPriceRepository;
import com.example.diamondstore.repository.DiamondRepository;
import com.example.diamondstore.repository.WarrantyRepository;
import com.example.diamondstore.request.putRequest.DiamondPutRequest;
import com.example.diamondstore.specification.DiamondSpecification;

@Service
public class DiamondService {

    @Autowired
    private DiamondRepository diamondRepository;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private WarrantyRepository warrantyRepository;

    @Autowired
    private DiamondPriceRepository diamondPriceRepository;

    @Autowired
    private CertificateService certificateService;

    public List<Diamond> getAllDiamondsGrossPriceIsNull() {
        List<Diamond> diamonds = diamondRepository.findAllByGrossDiamondPriceNotNull();
        return diamonds;
    }

    public List<Diamond> getAllDiamonds() {
        return diamondRepository.findAll();
    }

    public Diamond getDiamondById(String diamondID) {
        return diamondRepository.findByDiamondID(diamondID);
    }

    @PostConstruct
    public void updateDiamondStatusesOnStartup() {
        updateDiamondStatusesAuto();
    }

    @Scheduled(cron = "0 */30 * * * *")
    public void updateDiamondStatusesAuto() {
        List<Diamond> diamonds = diamondRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Diamond diamond : diamonds) {
            if (diamond.getQuantity() == 0) {
                diamond.setStatus("Hết hàng");
            } else {
                diamond.setStatus("Còn hàng");
            }
            diamondRepository.save(diamond);
        }
    }

    public ResponseEntity<Map<String, String>> createDiamond(Diamond diamond) {
        Diamond existingDiamondByID = diamondRepository.findByDiamondID(diamond.getDiamondID());
        if (existingDiamondByID != null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Kim cương đã tồn tại"));
        }

        // Calculate weight based on carat size
        BigDecimal sizeDividedBy = diamond.getCaratSize().divide(new BigDecimal(6.5), MathContext.DECIMAL128);
        diamond.setWeight(sizeDividedBy.pow(2));

        boolean priceMismatch = false;
        BigDecimal diamondEntryPriceInput = diamond.getDiamondEntryPrice();
        DiamondPrice diamondPrice = diamondPriceRepository.findAllByCaratSizeAndColorAndClarity(
                diamond.getCaratSize(), diamond.getColor(), diamond.getClarity());

        if (diamondPrice == null) {
            if (diamondEntryPriceInput == null) {
                diamond.setDiamondEntryPrice(BigDecimal.ZERO);
                diamondRepository.save(diamond);
                return ResponseEntity.ok(Collections.singletonMap("message", "Kim cương tạo thành công, nhưng chưa có giá. Vui lòng thêm giá cho kim cương này."));
            }
            diamond.setDiamondEntryPrice(diamondEntryPriceInput);
            diamond.setDiamondPrice(null);
        } else {
            BigDecimal diamondEntryPriceDB = diamondPrice.getDiamondEntryPrice();
            System.out.println("diamondEntryPriceInput: " + diamondEntryPriceInput);
            System.out.println("diamondEntryPriceDB: " + diamondEntryPriceDB);
            if (diamondEntryPriceInput != null && diamondEntryPriceInput.compareTo(diamondEntryPriceDB) != 0) {
                priceMismatch = true;
                diamond.setDiamondEntryPrice(null);
                diamond.setGrossDiamondPrice(null);
            } else {
                diamond.setDiamondEntryPrice(diamondEntryPriceDB);
            }
            diamond.setDiamondPrice(diamondPrice);
        }

        if(diamond.getQuantity() < 0) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Số lượng không hợp lệ"));
        }

        if (diamond.getCertificationID() != null && diamondRepository.existsByCertificationID(diamond.getCertificationID())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Chứng chỉ đã được gán cho một kim cương khác"));
        }

        if (diamond.getWarrantyID() != null && diamondRepository.existsByWarrantyID(diamond.getWarrantyID())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Bảo hành đã được gán cho một kim cương khác"));
        }

        if (diamond.getQuantity() != 0) {
            diamond.setStatus("Còn hàng");
        } else {
            diamond.setStatus("Hết hàng");
        }

        // Calculate gross diamond price = diamond price * 1.1
        if (diamond.getDiamondEntryPrice() != null) {
            BigDecimal grossDiamondPrice = diamond.getDiamondEntryPrice().multiply(new BigDecimal(1.1));
            diamond.setGrossDiamondPrice(grossDiamondPrice);
        } else {
            diamond.setGrossDiamondPrice(null);
        }

        updateDiamondStatusesAuto();
        diamondRepository.save(diamond);

        if (priceMismatch) {
            return ResponseEntity.ok(Collections.singletonMap("message", "Tạo thành công nhưng có sự chênh lệch giá giữa giá nhập và giá trong bảng. Giá không được lưu"));
        }
        return ResponseEntity.ok(Collections.singletonMap("message", "Tạo thành công"));
    }

    public Map<String, String> updateDiamond(String diamondID, DiamondPutRequest diamondPutRequest) {
        Diamond existingDiamond = diamondRepository.findByDiamondID(diamondID);
        if (existingDiamond == null) {
            return Collections.singletonMap("message", "Không tìm thấy kim cương");
        }

        existingDiamond.setWarrantyID(diamondPutRequest.getWarrantyID());
        existingDiamond.setCertificationID(diamondPutRequest.getCertificationID());
        existingDiamond.setDiamondName(diamondPutRequest.getDiamondName());
        existingDiamond.setDiamondImage(diamondPutRequest.getDiamondImage());
        existingDiamond.setOrigin(diamondPutRequest.getOrigin());
        existingDiamond.setCut(diamondPutRequest.getCut());
        existingDiamond.setShape(diamondPutRequest.getShape());
        existingDiamond.setColor(diamondPutRequest.getColor());
        existingDiamond.setCaratSize(diamondPutRequest.getCaratSize());
        existingDiamond.setClarity(diamondPutRequest.getClarity());
        existingDiamond.setQuantity(diamondPutRequest.getQuantity());

        if(existingDiamond.getQuantity() != 0) {
            existingDiamond.setStatus("Còn hàng");
        } else {
            existingDiamond.setStatus("Hết hàng");
        }

        // Calculate weight based on carat size
        BigDecimal sizeDividedBy = diamondPutRequest.getCaratSize().divide(new BigDecimal(6.5), MathContext.DECIMAL128);
        existingDiamond.setWeight(sizeDividedBy.pow(2));

        boolean priceMismatch = false;
        BigDecimal diamondEntryPriceInput = diamondPutRequest.getDiamondEntryPrice();
        DiamondPrice diamondPrice = diamondPriceRepository.findAllByCaratSizeAndColorAndClarity(
                diamondPutRequest.getCaratSize(), diamondPutRequest.getColor(), diamondPutRequest.getClarity());

        // If no matching diamond price is found
        if (diamondPrice == null) {
            if (diamondEntryPriceInput == null) {
                existingDiamond.setDiamondEntryPrice(null);
                existingDiamond.setDiamondPrice(null);
            } else {
                existingDiamond.setDiamondEntryPrice(diamondEntryPriceInput); // Use input price if provided
                existingDiamond.setDiamondPrice(diamondPrice);
            }
        } else {
            BigDecimal diamondEntryPriceDB = diamondPrice.getDiamondEntryPrice();
            existingDiamond.setDiamondEntryPrice(diamondEntryPriceDB); // Always use DB price if a match is found

            // Check for price mismatch only if input price is provided
            if (diamondEntryPriceInput != null && diamondEntryPriceInput.compareTo(diamondEntryPriceDB) != 0) {
                priceMismatch = true;
            }
        }

        // Calculate gross diamond price = diamond price * 1.1
        if (existingDiamond.getDiamondEntryPrice() != null) {
            BigDecimal grossDiamondPrice = existingDiamond.getDiamondEntryPrice().multiply(new BigDecimal(1.1));
            existingDiamond.setGrossDiamondPrice(grossDiamondPrice);
        } else {
            existingDiamond.setGrossDiamondPrice(null); // Ensure gross price is null if entry price is null
        }

        updateDiamondStatusesAuto();
        diamondRepository.save(existingDiamond);

        if (priceMismatch) {
            return Collections.singletonMap("message", "Cập nhật thành công nhưng có sự chênh lệch giá giữa giá nhập và giá trong bảng. Giá không được lưu");
        }

        return Collections.singletonMap("message", "Cập nhật thành công");
    }

    @Transactional
    public ResponseEntity<Map<String, String>> deleteDiamonds(@RequestBody List<String> diamondIDs) {
        // Filter out non-existing diamonds
        List<String> existingDiamondIDs = diamondIDs.stream()
                .filter(diamondID -> diamondRepository.existsById(diamondID))
                .collect(Collectors.toList());

        if (!existingDiamondIDs.isEmpty()) {
            // Delete related certificates first
            existingDiamondIDs.forEach(diamondID -> certificateRepository.deleteByDiamondID(diamondID));
            existingDiamondIDs.forEach(diamondID -> warrantyRepository.deleteByDiamondID(diamondID));

            // Delete diamonds
            diamondRepository.deleteAllById(existingDiamondIDs);

            return ResponseEntity.ok().body(Collections.singletonMap("message", "Xóa các viên kim cương thành công"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Không tìm thấy viên kim cương để xóa"));
        }
    }

    public Page<Diamond> getAllDiamondsPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return diamondRepository.findAll(pageable);
    }

    public List<Diamond> searchDiamondsWithFilters(
            Float mindiamondEntryPrice,
            Float maxdiamondEntryPrice,
            String origin,
            String cut,
            String shape,
            String color,
            BigDecimal minCaratSize,
            BigDecimal maxCaratSize,
            BigDecimal minWeight,
            BigDecimal maxWeight,
            String clarity,
            String diamondNameLike) {

        Specification<Diamond> spec = Specification.where(null);

        if (mindiamondEntryPrice != null) {
            spec = spec.and(DiamondSpecification.hasMinDiamondEntryPrice(mindiamondEntryPrice));
        }
        if (maxdiamondEntryPrice != null) {
            spec = spec.and(DiamondSpecification.hasMaxDiamondEntryPrice(maxdiamondEntryPrice));
        }
        if (origin != null) {
            spec = spec.and(DiamondSpecification.hasOrigin(origin));
        }
        if (cut != null) {
            spec = spec.and(DiamondSpecification.hasCut(cut));
        }
        if (shape != null) {
            spec = spec.and(DiamondSpecification.hasShape(shape));
        }
        if (color != null) {
            spec = spec.and(DiamondSpecification.hasColor(color));
        }
        if (clarity != null) {
            spec = spec.and(DiamondSpecification.hasClarity(clarity));
        }
        if (minCaratSize != null) {
            spec = spec.and(DiamondSpecification.hasMinCaratSize(minCaratSize));
        }
        if (maxCaratSize != null) {
            spec = spec.and(DiamondSpecification.hasMaxCaratSize(maxCaratSize));
        }
        if (minWeight != null) {
            spec = spec.and(DiamondSpecification.hasMinCaratWeight(minWeight));
        }
        if (maxWeight != null) {
            spec = spec.and(DiamondSpecification.hasMaxCaratWeight(maxWeight));
        }
        if (diamondNameLike != null) {
            return diamondRepository.findByDiamondNameLike("%" + diamondNameLike + "%");
        }

        return diamondRepository.findAll(spec);
    }

    public Page<Diamond> searchDiamondsWithFiltersPaged(
            Float minDiamondEntryPrice,
            Float maxDiamondEntryPrice,
            String origin,
            String cut,
            String shape,
            String color,
            BigDecimal minCaratSize,
            BigDecimal maxCaratSize,
            BigDecimal minWeight,
            BigDecimal maxWeight,
            String clarity,
            String diamondName,
            int page,
            int size) {

        Specification<Diamond> spec = Specification.where(null);

        if (minDiamondEntryPrice != null) {
            spec = spec.and(DiamondSpecification.hasMinDiamondEntryPrice(minDiamondEntryPrice));
        }
        if (maxDiamondEntryPrice != null) {
            spec = spec.and(DiamondSpecification.hasMaxDiamondEntryPrice(maxDiamondEntryPrice));
        }
        if (origin != null) {
            spec = spec.and(DiamondSpecification.hasOrigin(origin));
        }
        if (cut != null) {
            spec = spec.and(DiamondSpecification.hasCut(cut));
        }
        if (shape != null) {
            spec = spec.and(DiamondSpecification.hasShape(shape));
        }
        if (color != null) {
            spec = spec.and(DiamondSpecification.hasColor(color));
        }
        if (clarity != null) {
            spec = spec.and(DiamondSpecification.hasClarity(clarity));
        }
        if (minCaratSize != null) {
            spec = spec.and(DiamondSpecification.hasMinCaratSize(minCaratSize));
        }
        if (maxCaratSize != null) {
            spec = spec.and(DiamondSpecification.hasMaxCaratSize(maxCaratSize));
        }
        if (minWeight != null) {
            spec = spec.and(DiamondSpecification.hasMinCaratWeight(minWeight));
        }
        if (maxWeight != null) {
            spec = spec.and(DiamondSpecification.hasMaxCaratWeight(maxWeight));
        }
        if (diamondName != null) {
            spec = spec.and(DiamondSpecification.hasDiamondNameIgnoreCase(diamondName));
        }

        Pageable pageable = PageRequest.of(page - 1, size);
        return diamondRepository.findAll(spec, pageable);
    }

}
