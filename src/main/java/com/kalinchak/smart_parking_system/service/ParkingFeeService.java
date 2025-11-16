package com.kalinchak.smart_parking_system.service;

import com.kalinchak.smart_parking_system.model.VehicleType;

import java.math.BigDecimal;

public interface ParkingFeeService {

    BigDecimal calculateFee(final VehicleType type, final long durationMinutes);
}
