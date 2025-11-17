package com.kalinchak.smart_parking_system.mapper;

import com.kalinchak.smart_parking_system.model.ParkingLevel;
import com.kalinchak.smart_parking_system.model.ParkingSlot;
import com.kalinchak.smart_parking_system.model.dto.ParkingSlotDto;

public interface ParkingSlotMapper {
    ParkingSlot fromDto(ParkingSlotDto dto, ParkingLevel level);
}
