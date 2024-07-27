package com.example.diamondstore.controller.Certificate;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.diamondstore.model.Certificate;
import com.example.diamondstore.service.CertificateService;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<Iterable<Certificate>> getCertificates() {
        return ResponseEntity.ok(certificateService.getAllCertificates());
    }

    @GetMapping("/get-paging")
    public ResponseEntity<Page<Certificate>> getAllCertificatePaged(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(certificateService.getAllCertificatesPaged(page, size));
    }

    @GetMapping("/get-by-id/{certificateID}")
    public ResponseEntity<?> getCertificateById(@PathVariable String certificateID) {
        return certificateService.getCertificateById(certificateID);
    }

    @GetMapping(value = "/get-certificate-image/{certificateID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getCertificateImg(@PathVariable String certificateID) {
        return certificateService.getCertificateImg(certificateID);
    }
}
