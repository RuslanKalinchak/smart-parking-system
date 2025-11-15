package com.kalinchak.smart_parking_system.service;

import java.math.BigDecimal;

public interface ParkingFeeStrategy {
    BigDecimal calculate(long durationMinutes);
}
