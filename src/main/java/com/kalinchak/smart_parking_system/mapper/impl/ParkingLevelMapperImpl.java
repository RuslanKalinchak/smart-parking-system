package com.kalinchak.smart_parking_system.mapper.impl;

import com.kalinchak.smart_parking_system.mapper.ParkingLevelMapper;
import com.kalinchak.smart_parking_system.mapper.ParkingSlotMapper;
import com.kalinchak.smart_parking_system.model.ParkingLevel;
import com.kalinchak.smart_parking_system.model.ParkingLot;
import com.kalinchak.smart_parking_system.model.ParkingSlot;
import com.kalinchak.smart_parking_system.model.dto.ParkingLevelDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ParkingLevelMapperImpl implements ParkingLevelMapper {

    private final ParkingSlotMapper parkingSlotMapper;

    @Override
    public ParkingLevel fromDto(ParkingLevelDto dto, ParkingLot lot) {
        ParkingLevel level = ParkingLevel.builder()
                .levelNumber(dto.levelNumber())
                .parkingLot(lot)
                .build();

        List<ParkingSlot> slots = dto.slots().stream()
                .map(slot -> parkingSlotMapper.fromDto(slot, level))
                .toList();

        level.setSlots(slots);
        return level;
    }
}
