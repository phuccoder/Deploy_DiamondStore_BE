package com.example.diamondstore.controller.AccumulatePoints;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.diamondstore.model.AccumulatePoints;
import com.example.diamondstore.request.AccumulatePointsRequest;
import com.example.diamondstore.service.AccumulatePointsService;

@RestController
@RequestMapping("/api/admin/accumulate-points")
public class AccumulatePointsControllerAdmin {

    private final AccumulatePointsService accumulatePointsService;

    public AccumulatePointsControllerAdmin(AccumulatePointsService accumulatePointsService) {
        this.accumulatePointsService = accumulatePointsService;
    }

    @GetMapping("/{accountID}")
    public ResponseEntity<AccumulatePoints> getCustomerById_Admin(@PathVariable Integer accountID) {
        Optional<AccumulatePoints> accumulatePoints = accumulatePointsService.getAccumulatePointsById(accountID);
        if (accumulatePoints.isPresent()) {
            return ResponseEntity.ok(accumulatePoints.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get-total")
    public ResponseEntity<Long> getTotalCustomers() {
        long totalCustomers = accumulatePointsService.getTotalAccumulatePoints();
        return new ResponseEntity<>(totalCustomers, HttpStatus.OK);
    }

    @PutMapping(value = "/update/{accountID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> updateCustomer(@PathVariable Integer accountID, @RequestBody AccumulatePointsRequest updatedAccumulatePointsRequest) {
        boolean isUpdated = accumulatePointsService.updateAccumulatePoints(accountID, updatedAccumulatePointsRequest);
        if (isUpdated) {
            return ResponseEntity.ok(Collections.singletonMap("message", "Cập nhật thành công"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{accountID}")
    public ResponseEntity<Map<String, String>> deleteCustomer(@PathVariable Integer accountID) {
        return accumulatePointsService.deleteAccumulatePoints(accountID);
    }
}
