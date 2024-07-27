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

import com.example.diamondstore.model.Certificate;
import com.example.diamondstore.model.Diamond;
import com.example.diamondstore.repository.CertificateRepository;
import com.example.diamondstore.repository.DiamondRepository;
import com.example.diamondstore.request.putRequest.CertificatePutRequest;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private DiamondRepository diamondRepository;


    public Iterable<Certificate> getAllCertificates() {
        return certificateRepository.findAll();
    }

    @PostConstruct
    public void updateCertificateStatusesOnStartup() {
        updateCertificateStatusesAuto();
    }

    @Scheduled(cron = "0 0 * * * *") // Chạy mỗi giờ
    public void updateCertificateStatusesAuto() {
        List<Certificate> certificates = certificateRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Certificate certificate : certificates) {
            if (certificate.getExpirationDate().isBefore(now)) {
                certificate.setCertificateStatus("Hết Hạn");
            } else {
                certificate.setCertificateStatus("Còn Hạn");
            }
            certificateRepository.save(certificate);
        }
    }

     private void updateCertificateStatus(Certificate certificate) {
        LocalDateTime now = LocalDateTime.now();
        if (certificate.getExpirationDate().isBefore(now)) {
            certificate.setCertificateStatus("Hết Hạn");
        } else {
            certificate.setCertificateStatus("Còn Hạn");
        }
    }

    public ResponseEntity<?> getCertificateById(String certificateID) {
        // validate certificateID
        if (!validateCertificateID(certificateID)) {
            return new ResponseEntity<>(Collections.singletonMap("message", "Mã chứng chỉ không hợp lệ"), HttpStatus.BAD_REQUEST);
        }

        Certificate certificate = certificateRepository.findByCertificateID(certificateID);
        if (certificate == null) {
            return new ResponseEntity<>(Collections.singletonMap("message", "ID không tồn tại"), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(certificate);
    }

    public Page<Certificate> getAllCertificatesPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page -1, size);
        return certificateRepository.findAll(pageable);
    }

    @Transactional
    public ResponseEntity<Map<String, String>> createCertificate(Certificate certificate) {
        // validate certificateID
        String certificateID = certificate.getCertificateID();
        if (!validateCertificateID(certificateID)) {
            return new ResponseEntity<>(Collections.singletonMap("message", "Mã chứng chỉ không hợp lệ"), HttpStatus.BAD_REQUEST);
        }

        Certificate existingCertificate = certificateRepository.findByCertificateID(certificate.getCertificateID());
        if (existingCertificate != null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Chứng chỉ đã tồn tại"));
        }

        updateCertificateStatus(certificate);
        certificateRepository.save(certificate);
        Diamond diamond = diamondRepository.findById(certificate.getDiamondID()).orElse(null);
        if (diamond != null) {
            diamond.setCertificationID(certificate.getCertificateID());
            diamondRepository.save(diamond);
        } else {
            throw new RuntimeException("Viên kim cương không tồn tại");
        }

        return ResponseEntity.ok(Collections.singletonMap("message", "Tạo thành công"));
    }

    public ResponseEntity<Map<String, String>> updateCertificate(String certificateID, CertificatePutRequest certificatePutRequest) {
        Certificate existingCertificate = certificateRepository.findByCertificateID(certificateID);
        if (existingCertificate == null) {
            return ResponseEntity.notFound().build();
        }
        existingCertificate.setDiamondID(certificatePutRequest.getDiamondID());
        existingCertificate.setExpirationDate(certificatePutRequest.getExpirationDate());
        existingCertificate.setcertificateImage(certificatePutRequest.getCertificateImage());
        updateCertificateStatus(existingCertificate);
        certificateRepository.save(existingCertificate);
        return ResponseEntity.ok(Collections.singletonMap("message", "Cập nhật thành công"));
    }

    @Transactional
    public ResponseEntity<Map<String, String>> deleteCertificates(@RequestBody List<String> certificateIDs) {
        // check certificateIDs isEmpty()
        if (certificateIDs.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Không có mã chứng chỉ để xóa"));
        }

        // validate certificateIDs
        for (String certificateID : certificateIDs) {
            if (!validateCertificateID(certificateID)) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Mã chứng chỉ không hợp lệ"));
            }
        }

        // Filter out non-existing certificates
        List<String> existingCertificateIDs = certificateIDs.stream()
                .filter(certificateID -> certificateRepository.existsById(certificateID))
                .collect(Collectors.toList());

        // If no existing certificates are found
        if (existingCertificateIDs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Không tìm thấy chứng chỉ để xóa"));
        }

        // Find and update diamonds associated with the certificates
        List<Diamond> diamondsToUpdate = diamondRepository.findAllByCertificationIDIn(existingCertificateIDs);
        for (Diamond diamond : diamondsToUpdate) {
            diamond.setCertificationID(null);
        }
        diamondRepository.saveAll(diamondsToUpdate);

        // Delete certificates
        certificateRepository.deleteAllById(existingCertificateIDs);

        return ResponseEntity.ok().body(Collections.singletonMap("message", "Xóa các chứng chỉ thành công"));
    }

    public ResponseEntity<?> getCertificateImg(String certificateID) {
        // validate certificateID
        if (!validateCertificateID(certificateID)) {
            return new ResponseEntity<>(Collections.singletonMap("message", "Mã chứng chỉ không hợp lệ"), HttpStatus.BAD_REQUEST);
        }

        Certificate certificate = certificateRepository.findByCertificateID(certificateID);
        if (certificate == null) {
            return new ResponseEntity<>(Collections.singletonMap("message", "ID không tồn tại"), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(Collections.singletonMap("certificateImage", certificate.getcertificateImage()));
    }

    //validate certificateID
    public boolean validateCertificateID(String certificateID) {
        // jewelry has form: JID-
        if (!certificateID.startsWith("CID-")) {
            return false;
        }
        return true;
    }
}
