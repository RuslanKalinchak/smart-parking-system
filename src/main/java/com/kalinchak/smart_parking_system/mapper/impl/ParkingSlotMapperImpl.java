package com.kalinchak.smart_parking_system.mapper.impl;

import com.kalinchak.smart_parking_system.mapper.ParkingSlotMapper;
import com.kalinchak.smart_parking_system.model.ParkingLevel;
import com.kalinchak.smart_parking_system.model.ParkingSlot;
import com.kalinchak.smart_parking_system.model.SlotStatus;
import com.kalinchak.smart_parking_system.model.dto.ParkingSlotDto;
import org.springframework.stereotype.Component;

@Component
public class ParkingSlotMapperImpl implements ParkingSlotMapper {

    @Override
    public ParkingSlot fromDto(ParkingSlotDto dto, ParkingLevel level) {
        return ParkingSlot.builder()
                .slotCode(dto.slotCode())
                .type(dto.type())
                .status(SlotStatus.AVAILABLE)
                .parkingLevel(level)
                .build();
    }
}
