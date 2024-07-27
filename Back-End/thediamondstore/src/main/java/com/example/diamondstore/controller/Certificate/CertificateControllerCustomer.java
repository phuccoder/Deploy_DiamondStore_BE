package com.example.diamondstore.controller.Certificate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.diamondstore.service.CertificateService;

@RestController
@RequestMapping("/api/customer/certificates")
public class CertificateControllerCustomer {

    private final CertificateService certificateService;

    public CertificateControllerCustomer(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping(value = "/get-certificate-image/{certificateID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getCertificateImg(@PathVariable String certificateID) {
        return certificateService.getCertificateImg(certificateID);
    }
}
