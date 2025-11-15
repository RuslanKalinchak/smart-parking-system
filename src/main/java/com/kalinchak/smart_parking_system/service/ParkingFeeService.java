package com.kalinchak.smart_parking_system.service;

import com.kalinchak.smart_parking_system.model.VehicleType;
import com.kalinchak.smart_parking_system.service.impl.ParkingFeeStrategyFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class ParkingFeeService {

    public BigDecimal calculateFee(final VehicleType type, final long durationMinutes) {

        ParkingFeeStrategy parkingFeeStrategy = ParkingFeeStrategyFactory.getStrategy(type);

        if (parkingFeeStrategy == null) {
            throw new IllegalArgumentException("No fee strategy defined for vehicle type: " + type);
        }

        return parkingFeeStrategy.calculate(durationMinutes);
    }
}
