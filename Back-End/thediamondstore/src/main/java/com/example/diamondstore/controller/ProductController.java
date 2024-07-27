package com.example.diamondstore.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.diamondstore.service.ProductionService;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductionService productionService;

    // guest
    @GetMapping(value = "/guest/products/get-all", produces = "application/json;charset=UTF-8")
    public Map<String, Object> getAllProduction_Guest() {
        return productionService.getAllProduction();
    }

    // guest
    // API search theo tên, giá diamond và jewelry
    @GetMapping(value = "/guest/products/search/get-paging", produces = "application/json;charset=UTF-8")
    public Map<String, Object> searchAndFilterProductionPaged_Guest(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Float minPrice,
            @RequestParam(required = false) Float maxPrice,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return productionService.searchAndFilterProductionPaged(name, minPrice, maxPrice, page, size);
    }

    // guest
    @GetMapping(value = "/guest/products/get-paging", produces = "application/json;charset=UTF-8")
    public Map<String, Object> getPagedProduction(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return productionService.getPagedProduction(page, size);
    }

    // guest
    @GetMapping(value = "/guest/products/total", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Long> getTotalProduction_Guest() {
        long totalProduction = productionService.getTotalProduction();
        return new ResponseEntity<>(totalProduction, HttpStatus.OK);
    }

    // admin + manager
    @GetMapping(value = "/products/total-products", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, Object>> getTotalProductionCount() {
        Map<String, Object> response = productionService.getTotalProductionCount();
        return ResponseEntity.ok(response);
    }
}
