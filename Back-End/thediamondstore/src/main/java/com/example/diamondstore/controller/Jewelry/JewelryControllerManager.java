package com.example.diamondstore.controller.Jewelry;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.diamondstore.model.Jewelry;
import com.example.diamondstore.request.putRequest.JewelryPutRequest;
import com.example.diamondstore.service.JewelryService;

@RestController
@RequestMapping("/api/manager/jewelry-management/jewelries")
public class JewelryControllerManager {

    private final JewelryService jewelryService;

    public JewelryControllerManager(JewelryService jewelryService) {
        this.jewelryService = jewelryService;
    }

    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> createJewelryAdmin(@RequestBody Jewelry jewelry) {
        return jewelryService.createJewelry(jewelry);
    }

    @PutMapping(value = "/update/{jewelryID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> updateJewelryAdmin(@PathVariable String jewelryID, @RequestBody JewelryPutRequest jewelryPutRequest) {
        return jewelryService.updateJewelry(jewelryID, jewelryPutRequest);
    }

    @DeleteMapping(value = "/delete", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> deleteJewelrysAdmin(@RequestBody List<String> jewelryIDs) {
        try {
            jewelryService.deleteJewelry(jewelryIDs);
            return ResponseEntity.ok(Collections.singletonMap("message", "Xóa các trang sức thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }
}
