package com.kalinchak.smart_parking_system.mapper;

import com.kalinchak.smart_parking_system.model.entity.ParkingLot;
import com.kalinchak.smart_parking_system.model.dto.ParkingLotDto;

public interface ParkingLotMapper {

    ParkingLot fromDto(ParkingLotDto dto);
}
