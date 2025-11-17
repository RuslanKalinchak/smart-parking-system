package com.kalinchak.smart_parking_system.service.impl;

import com.kalinchak.smart_parking_system.model.VehicleType;
import com.kalinchak.smart_parking_system.service.ParkingFeeService;
import com.kalinchak.smart_parking_system.service.ParkingFeeStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ParkingFeeServiceImpl implements ParkingFeeService {

    @Override
    public BigDecimal calculateFee(final VehicleType type, final long durationMinutes) {

        ParkingFeeStrategy parkingFeeStrategy = ParkingFeeStrategyFactory.getStrategy(type);
        return parkingFeeStrategy.calculate(durationMinutes);
    }
}
