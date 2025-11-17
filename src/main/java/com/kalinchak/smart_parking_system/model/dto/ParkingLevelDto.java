package com.kalinchak.smart_parking_system.model.dto;

import com.kalinchak.smart_parking_system.model.entity.ParkingLevel;

import java.util.List;

public record ParkingLevelDto(Long id, int levelNumber, List<ParkingSlotDto> slots) {

    public ParkingLevelDto(final ParkingLevel parkingLevel) {
        this(parkingLevel.getId(), parkingLevel.getLevelNumber(),
                parkingLevel.getSlots().stream()
                        .map(ParkingSlotDto::new)
                        .toList());
    }
}
