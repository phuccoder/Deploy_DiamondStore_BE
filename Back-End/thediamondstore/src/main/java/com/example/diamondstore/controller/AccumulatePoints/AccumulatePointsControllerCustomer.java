package com.example.diamondstore.controller.AccumulatePoints;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.diamondstore.model.AccumulatePoints;
import com.example.diamondstore.service.AccumulatePointsService;

@RestController
@RequestMapping("/api/customer/accumulate-points")
public class AccumulatePointsControllerCustomer {

    private final AccumulatePointsService customerService;

    public AccumulatePointsControllerCustomer(AccumulatePointsService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{accountID}")
    public ResponseEntity<AccumulatePoints> getAccumulatePointsById_Customer(@PathVariable Integer accountID) {
        Optional<AccumulatePoints> customer = customerService.getAccumulatePointsById(accountID);
        if (customer.isPresent()) {
            return ResponseEntity.ok(customer.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
