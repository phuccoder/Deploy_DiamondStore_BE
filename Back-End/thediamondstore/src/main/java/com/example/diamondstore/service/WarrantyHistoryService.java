package com.example.diamondstore.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.diamondstore.model.Certificate;
import com.example.diamondstore.model.WarrantyHistory;
import com.example.diamondstore.repository.WarrantyHistoryRepository;

@Service
public class WarrantyHistoryService {

    @Autowired
    private WarrantyHistoryRepository warrantyHistoryRepository;

    @PostConstruct
    public void updateWarrantyHistoryStatuses() {
        updateWarrantyHistoryStatusesAuto();
    }

    @Scheduled(cron = "0 0 * * * *")
    public void updateWarrantyHistoryStatusesAuto() {
        List<WarrantyHistory> warrantyHistories = warrantyHistoryRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (WarrantyHistory warrantyHistory : warrantyHistories) {
            if (warrantyHistory.getExpirationDate().isBefore(now)) {
                warrantyHistory.setWarrantyStatus("Hết hiệu lực");
            } else {
                warrantyHistory.setWarrantyStatus("Đã kích hoạt");
            }
            warrantyHistoryRepository.save(warrantyHistory);
        }
    }

}
