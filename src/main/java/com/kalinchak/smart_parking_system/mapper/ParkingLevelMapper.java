package com.kalinchak.smart_parking_system.mapper;

import com.kalinchak.smart_parking_system.model.ParkingLevel;
import com.kalinchak.smart_parking_system.model.ParkingLot;
import com.kalinchak.smart_parking_system.model.dto.ParkingLevelDto;

public interface ParkingLevelMapper {

    ParkingLevel fromDto(ParkingLevelDto dto, ParkingLot lot);
}
