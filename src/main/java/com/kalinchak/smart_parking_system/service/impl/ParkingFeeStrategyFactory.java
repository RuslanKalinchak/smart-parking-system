package com.kalinchak.smart_parking_system.service.impl;

import com.kalinchak.smart_parking_system.model.VehicleType;
import com.kalinchak.smart_parking_system.service.ParkingFeeStrategy;

import java.util.Map;

public class ParkingFeeStrategyFactory {

    private static final Map<VehicleType, ParkingFeeStrategy> STRATEGIES = Map.of(
            VehicleType.MOTORCYCLE, new MotorcycleFeeStrategy(),
            VehicleType.CAR, new CarFeeStrategy(),
            VehicleType.TRUCK, new TruckFeeStrategy()
    );

    public static ParkingFeeStrategy getStrategy(VehicleType type) throws IllegalArgumentException {
        return STRATEGIES.get(type);
    }
}
