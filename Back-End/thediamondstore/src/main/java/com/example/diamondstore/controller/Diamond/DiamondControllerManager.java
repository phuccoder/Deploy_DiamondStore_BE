package com.example.diamondstore.controller.Diamond;

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

import com.example.diamondstore.model.Diamond;
import com.example.diamondstore.request.putRequest.DiamondPutRequest;
import com.example.diamondstore.service.DiamondService;

@RestController
@RequestMapping("/api/manager/diamond-management/diamonds")
public class DiamondControllerManager {

    private final DiamondService diamondService;

    public DiamondControllerManager(DiamondService diamondService) {
        this.diamondService = diamondService;
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> createDiamond_Manager(@RequestBody Diamond diamond) {
        return diamondService.createDiamond(diamond);
    }

    @PutMapping(value = "/update/{diamondID}")
    public ResponseEntity<Map<String, String>> updateDiamond_Manager(@PathVariable String diamondID, @RequestBody DiamondPutRequest diamondPutRequest) {
        Map<String, String> response = diamondService.updateDiamond(diamondID, diamondPutRequest);
        if (response.containsKey("message") && response.get("message").equals("Không tìm thấy kim cương")) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/delete", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, String>> deleteDiamonds_Manager(@RequestBody List<String> diamondIDs) {
        try {
            diamondService.deleteDiamonds(diamondIDs);
            return ResponseEntity.ok(Collections.singletonMap("message", "Xóa các kim cương thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }
}
