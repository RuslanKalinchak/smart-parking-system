package com.kalinchak.smart_parking_system.mapper;

import com.kalinchak.smart_parking_system.model.entity.ParkingLevel;
import com.kalinchak.smart_parking_system.model.entity.ParkingSlot;
import com.kalinchak.smart_parking_system.model.dto.ParkingSlotDto;

public interface ParkingSlotMapper {
    ParkingSlot fromDto(ParkingSlotDto dto, ParkingLevel level);
}
