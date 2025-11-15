package com.kalinchak.smart_parking_system.service.impl;

import com.kalinchak.smart_parking_system.service.ParkingFeeStrategy;

import java.math.BigDecimal;

public class CarFeeStrategy implements ParkingFeeStrategy {

    @Override
    public BigDecimal calculate(long durationMinutes) {
        return BigDecimal.valueOf(durationMinutes / 60.0 * 2);
    }
}
