package com.example.diamondstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.diamondstore.model.AccumulatePoints;

public interface AccumulatePointsRepository extends JpaRepository<AccumulatePoints, Integer> {

    AccumulatePoints findByAccountID(Integer accountID);

    long count();

    Object deleteByAccountID(Integer accountID);
}
