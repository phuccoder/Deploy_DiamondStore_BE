package com.example.diamondstore.controller.Diamond;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.diamondstore.model.Diamond;
import com.example.diamondstore.service.DiamondService;

@RestController
@RequestMapping("/api/guest/diamonds")
public class DiamondControllerGuest {

    private final DiamondService diamondService;

    public DiamondControllerGuest(DiamondService diamondService) {
        this.diamondService = diamondService;
    }

    @GetMapping
    public ResponseEntity<List<Diamond>> getDiamonds_Guest() {
        return ResponseEntity.ok(diamondService.getAllDiamondsGrossPriceIsNull());
    }

    @GetMapping("/{diamondID}")
    public ResponseEntity<Diamond> getDiamond(@PathVariable String diamondID) {
        Diamond diamond = diamondService.getDiamondById(diamondID);
        if (diamond == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(diamond);
    }

    @GetMapping("/get-paging")
    public ResponseEntity<Page<Diamond>> getAllDiamondsPaged_Guest(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        Page<Diamond> pageDiamonds = diamondService.getAllDiamondsPaged(page, size);
        return ResponseEntity.ok(pageDiamonds);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Diamond>> searchDiamonds_Guest(
            @RequestParam(required = false) Float minDiamondPrice,
            @RequestParam(required = false) Float maxDiamondPrice,
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String cut,
            @RequestParam(required = false) String shape,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) BigDecimal minCaratSize,
            @RequestParam(required = false) BigDecimal maxCaratSize,
            @RequestParam(required = false) BigDecimal minWeight,
            @RequestParam(required = false) BigDecimal maxWeight,
            @RequestParam(required = false) String clarity,
            @RequestParam(required = false) String diamondNameLike) {

        List<Diamond> diamonds = diamondService.searchDiamondsWithFilters(
                minDiamondPrice, maxDiamondPrice, origin, cut, shape, color,
                minCaratSize, maxCaratSize, minWeight, maxWeight, clarity, diamondNameLike);

        return ResponseEntity.ok(diamonds);
    }

    @GetMapping("/search/get-paging")
    public ResponseEntity<Page<Diamond>> searchDiamondsPaged_Guest(
            @RequestParam(required = false) Float minDiamondPrice,
            @RequestParam(required = false) Float maxDiamondPrice,
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String cut,
            @RequestParam(required = false) String shape,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) BigDecimal minCaratSize,
            @RequestParam(required = false) BigDecimal maxCaratSize,
            @RequestParam(required = false) BigDecimal minWeight,
            @RequestParam(required = false) BigDecimal maxWeight,
            @RequestParam(required = false) String clarity,
            @RequestParam(required = false) String diamondName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Diamond> pageDiamonds = diamondService.searchDiamondsWithFiltersPaged(
                minDiamondPrice, maxDiamondPrice, origin, cut, shape, color,
                minCaratSize, maxCaratSize, maxWeight, maxWeight, clarity, diamondName,
                page, size);

        return ResponseEntity.ok(pageDiamonds);
    }
}
