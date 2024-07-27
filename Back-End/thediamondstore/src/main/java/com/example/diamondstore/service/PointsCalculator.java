package com.example.diamondstore.service;

import java.math.BigDecimal;

public class PointsCalculator {
     private static final BigDecimal POINTS_CONVERSION_RATE = new BigDecimal("10000");

    public static int calculatePoints(BigDecimal amount) {
        return amount.divide(POINTS_CONVERSION_RATE).intValue();
    }
}
