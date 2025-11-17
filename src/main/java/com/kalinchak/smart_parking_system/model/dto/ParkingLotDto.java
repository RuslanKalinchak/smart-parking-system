package com.kalinchak.smart_parking_system.model.dto;

import com.kalinchak.smart_parking_system.model.entity.ParkingLot;

import java.util.List;

public record ParkingLotDto(Long id, String name, List<ParkingLevelDto> parkingLevels) {

    public ParkingLotDto(final ParkingLot parkingLot) {
        this(parkingLot.getId(), parkingLot.getName(), parkingLot.getParkingLevels().stream().map(ParkingLevelDto::new).toList());
    }
}
