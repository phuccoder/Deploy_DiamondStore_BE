package com.example.diamondstore.controller.Jewelry;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.diamondstore.model.Jewelry;
import com.example.diamondstore.service.JewelryService;

@RestController
@RequestMapping("/api/jewelries")
public class JewelryController {

    private final JewelryService jewelryService;

    public JewelryController(JewelryService jewelryService) {
        this.jewelryService = jewelryService;
    }

    @GetMapping(value = "/get-all", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<Jewelry>> getAllJewelry_Manager() {
        return ResponseEntity.ok(jewelryService.getAllJewelry());
    }

}
