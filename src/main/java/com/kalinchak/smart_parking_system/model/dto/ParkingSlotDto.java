package com.kalinchak.smart_parking_system.model.dto;

import com.kalinchak.smart_parking_system.model.ParkingSlot;
import com.kalinchak.smart_parking_system.model.SlotType;

public record ParkingSlotDto(Long id, String slotCode, SlotType type, int levelNumber) {

    public ParkingSlotDto(final ParkingSlot parkingSlot) {
        this(parkingSlot.getId(), parkingSlot.getSlotCode(), parkingSlot.getType(), parkingSlot.getParkingLevel().getLevelNumber());
    }
}
