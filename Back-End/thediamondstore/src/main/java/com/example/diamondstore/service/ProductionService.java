package com.example.diamondstore.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.diamondstore.model.Diamond;
import com.example.diamondstore.model.Jewelry;
import com.example.diamondstore.repository.DiamondRepository;
import com.example.diamondstore.repository.JewelryRepository;
import com.example.diamondstore.specification.DiamondSpecification;
import com.example.diamondstore.specification.JewelrySpecification;

@Service
public class ProductionService {

    @Autowired
    private DiamondRepository diamondRepository;

    @Autowired
    private JewelryRepository jewelryRepository;

    public Map<String, Object> getAllProduction() {
        List<Diamond> diamonds = diamondRepository.findAll();
        List<Jewelry> jewelry = jewelryRepository.findAll();

        Map<String, Object> response = new HashMap<>();
        response.put("diamonds", diamonds);
        response.put("jewelry", jewelry);

        return response;
    }

    public Map<String, Object> searchAndFilterProductionPaged(String name, Float minPrice, Float maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        // Build specifications for diamonds
        Specification<Diamond> diamondSpec = Specification.where(null);
        if (name != null) {
            diamondSpec = diamondSpec.and(DiamondSpecification.hasDiamondNameIgnoreCase(name));
        }
        if (minPrice != null) {
            diamondSpec = diamondSpec.and(DiamondSpecification.hasMinDiamondEntryPrice(minPrice));
        }
        if (maxPrice != null) {
            diamondSpec = diamondSpec.and(DiamondSpecification.hasMaxDiamondEntryPrice(maxPrice));
        }
        Page<Diamond> diamondPage = diamondRepository.findAll(diamondSpec, pageable);

        // Build specifications for jewelry
        Specification<Jewelry> jewelrySpec = Specification.where(null);
        if (name != null) {
            jewelrySpec = jewelrySpec.and(JewelrySpecification.hasJewelryNameIgnoreCase(name));
        }
        if (minPrice != null) {
            jewelrySpec = jewelrySpec.and(JewelrySpecification.hasMinJewelryEntryPrice(minPrice));
        }
        if (maxPrice != null) {
            jewelrySpec = jewelrySpec.and(JewelrySpecification.hasMaxJewelryEntryPrice(maxPrice));
        }
        Page<Jewelry> jewelryPage = jewelryRepository.findAll(jewelrySpec, pageable);

        // Build response
        Map<String, Object> response = new HashMap<>();
        response.put("diamonds", diamondPage.getContent());
        response.put("diamondsTotalPages", diamondPage.getTotalPages());
        response.put("diamondsTotalElements", diamondPage.getTotalElements());
        response.put("jewelry", jewelryPage.getContent());
        response.put("jewelryTotalPages", jewelryPage.getTotalPages());
        response.put("jewelryTotalElements", jewelryPage.getTotalElements());

        return response;
    }

    public Map<String, Object> getPagedProduction(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Diamond> diamondPage = diamondRepository.findAll(pageable);
        Page<Jewelry> jewelryPage = jewelryRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("diamonds", diamondPage.getContent());
        response.put("diamondsTotalPages", diamondPage.getTotalPages());
        response.put("diamondsTotalElements", diamondPage.getTotalElements());
        response.put("jewelry", jewelryPage.getContent());
        response.put("jewelryTotalPages", jewelryPage.getTotalPages());
        response.put("jewelryTotalElements", jewelryPage.getTotalElements());

        return response;
    }

    public long getTotalProduction() {
        long totalDiamonds = diamondRepository.count();
        long totalJewelry = jewelryRepository.count();
        return totalDiamonds + totalJewelry;
    }

    // lấy tổng số lượng diamond và jewelry
    public Map<String, Object> getTotalProductionCount() {
        long totalDiamonds = diamondRepository.count();
        long totalJewelry = jewelryRepository.count();

        Map<String, Object> response = new HashMap<>();
        response.put("number of Diamonds", totalDiamonds);
        response.put("number of Jewelry", totalJewelry);

        return response;
    }
}
