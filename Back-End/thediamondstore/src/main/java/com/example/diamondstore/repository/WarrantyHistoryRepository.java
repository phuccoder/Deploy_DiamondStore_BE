package com.example.diamondstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.diamondstore.model.WarrantyHistory;



public interface WarrantyHistoryRepository  extends JpaRepository<WarrantyHistory, Integer> {
    
}
