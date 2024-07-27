package com.example.diamondstore.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.diamondstore.model.Warranty;
import com.example.diamondstore.request.putRequest.WarrantyPutRequest;
import com.example.diamondstore.service.WarrantyService;

@RestController
@RequestMapping("/api")
public class WarrantyController {

    @Autowired
    private final WarrantyService warrantyService;

    public WarrantyController(WarrantyService warrantyService) {
        this.warrantyService = warrantyService;
    }

    // admin + manager
    @GetMapping(value = "/warranty-management/warranties/get-all", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Iterable<Warranty>> getWarranties() {
        return ResponseEntity.ok(warrantyService.getAllWarranties());
    }

    // manager
    @GetMapping(value = "/manager/warranty-management/warranties/{warrantyID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Warranty> getWarrantyManager(@PathVariable String warrantyID) {
        Warranty warranty = warrantyService.getWarrantyById(warrantyID);
        if (warranty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(warranty);
    }

    // customer
    @GetMapping(value = "/customer/warranties/{warrantyID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Warranty> getWarrantyCustomer(@PathVariable String warrantyID) {
        Warranty warranty = warrantyService.getWarrantyById(warrantyID);
        if (warranty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(warranty);
    }

    // admin + manager
    @GetMapping(value = "/warranty-management/warranties/get-paging", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<Warranty>> getAllWarrantiesPaged(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(warrantyService.getAllWarrantiesPaged(page, size));
    }

    // manager
    @PostMapping(value = "/manager/warranty-management/warranties/add", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> createWarrantyManager(@RequestBody Warranty warranty) {
        return warrantyService.createWarranty(warranty);
    }

    // manager
    @PutMapping(value = "/manager/warranty-management/warranties/update/{warrantyID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> updateWarrantyManager(@PathVariable String warrantyID, @RequestBody WarrantyPutRequest warrantyPutRequest) {
        return warrantyService.updateWarranty(warrantyID, warrantyPutRequest);
    }

    @DeleteMapping(value = "/manager/warranty-management/warranties/delete", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> deleteWarrantysManager(@RequestBody List<String> warrantyIDs) {
        try {
            warrantyService.deleteWarranty(warrantyIDs);
            return ResponseEntity.ok(Collections.singletonMap("message", "Xóa các giấy bảo hành thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    // admin + manager
    @GetMapping(value = "/warranty-management/warranties/get-warranty-image/{warrantyID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> getWarrantyImg(@PathVariable String warrantyID) {
        return warrantyService.getWarrantyImg(warrantyID);
    }

    // customer
    @GetMapping(value = "/customer/warranties/get-warranty-image/{warrantyID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> getWarrantyImgCustomer(@PathVariable String warrantyID) {
        return warrantyService.getWarrantyImg(warrantyID);
    }

    // admin + manager
    @GetMapping(value = "/warranty-management/warranties/get-jewelry-warranty", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<Warranty>> getWarrantiesByDiamondIDIsNull() {
        return ResponseEntity.ok(warrantyService.getWarrantiesByDiamondIDIsNull());
    }

    // admin + manager
    @GetMapping(value = "/warranty-management/warranties/get-diamond-warranty", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<Warranty>> getWarrantiesByJewelryIDIsNull() {
        return ResponseEntity.ok(warrantyService.getWarrantiesByJewelryIDIsNull());
    }

}
