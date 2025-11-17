package com.kalinchak.smart_parking_system.mapper.impl;

import com.kalinchak.smart_parking_system.mapper.ParkingLevelMapper;
import com.kalinchak.smart_parking_system.mapper.ParkingLotMapper;
import com.kalinchak.smart_parking_system.model.ParkingLevel;
import com.kalinchak.smart_parking_system.model.ParkingLot;
import com.kalinchak.smart_parking_system.model.dto.ParkingLotDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ParkingLotMapperImpl implements ParkingLotMapper {
    
    private final ParkingLevelMapper parkingLevelMapper;

    @Override
    public ParkingLot fromDto(ParkingLotDto dto) {
        ParkingLot lot = ParkingLot.builder()
                .name(dto.name())
                .build();

        List<ParkingLevel> parkingLevels = dto.parkingLevels().stream()
                .map(levelDto -> parkingLevelMapper.fromDto(levelDto, lot))
                .toList();

        lot.setParkingLevels(parkingLevels);
        return lot;
    }
}
