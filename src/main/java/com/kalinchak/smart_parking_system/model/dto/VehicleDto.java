package com.kalinchak.smart_parking_system.model.dto;

import com.kalinchak.smart_parking_system.model.entity.Vehicle;
import com.kalinchak.smart_parking_system.model.VehicleType;

public record VehicleDto(String licensePlate, VehicleType vehicleType) {

    public VehicleDto(final Vehicle vehicle) {
        this(vehicle.getLicensePlate(), vehicle.getVehicleType());
    }
}
