package com.example.diamondstore.controller.AccumulatePoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.diamondstore.model.AccumulatePoints;
import com.example.diamondstore.service.AccumulatePointsService;

@RestController
@RequestMapping("/api/accumulate-points")
public class AccumulatePointsController {

    @Autowired
    private AccumulatePointsService accumulatePointsService;

    @GetMapping("/get-all")
    public ResponseEntity<Iterable<AccumulatePoints>> getCustomers() {
        return ResponseEntity.ok(accumulatePointsService.getAllAccumulatePointss());
    }
}
