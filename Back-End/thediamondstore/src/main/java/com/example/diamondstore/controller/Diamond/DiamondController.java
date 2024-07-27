package com.example.diamondstore.controller.Diamond;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.diamondstore.model.Diamond;
import com.example.diamondstore.service.DiamondService;

@RestController
@RequestMapping("/api/diamonds")
public class DiamondController {

    private final DiamondService diamondService;

    public DiamondController(DiamondService diamondService) {
        this.diamondService = diamondService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Diamond>> getDiamonds() {
        return ResponseEntity.ok(diamondService.getAllDiamonds());
    }

}
