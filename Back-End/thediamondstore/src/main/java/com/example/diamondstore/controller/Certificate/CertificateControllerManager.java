package com.example.diamondstore.controller.Certificate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.diamondstore.model.Certificate;
import com.example.diamondstore.request.putRequest.CertificatePutRequest;
import com.example.diamondstore.service.CertificateService;

@RestController
@RequestMapping("/api/manager/certificate-management/certificates")
public class CertificateControllerManager {

    private final CertificateService certificateService;

    public CertificateControllerManager(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping("/{certificateID}")
    public ResponseEntity<?> getCertificate(@PathVariable String certificateID) {
        return certificateService.getCertificateById(certificateID);
    }

    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> createCertificate(@RequestBody Certificate certificate) {
        return certificateService.createCertificate(certificate);
    }

    @PutMapping(value = "/update/{certificateID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> updateCertificate(@PathVariable String certificateID, @RequestBody CertificatePutRequest certificatePutRequest) {
        return certificateService.updateCertificate(certificateID, certificatePutRequest);
    }

    @DeleteMapping(value = "/delete", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> deleteCertificates(@RequestBody List<String> certificateIDs) {
        try {
            certificateService.deleteCertificates(certificateIDs);
            return ResponseEntity.ok(Collections.singletonMap("message", "Xóa các giấy chứng chỉ thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @PostMapping("/update-status")
    public ResponseEntity<Map<String, String>> updateCertificateStatusesAuto() {
        certificateService.updateCertificateStatusesAuto();
        return ResponseEntity.ok(Collections.singletonMap("message", "Trạng thái chứng chỉ đã được cập nhật"));
    }
}
