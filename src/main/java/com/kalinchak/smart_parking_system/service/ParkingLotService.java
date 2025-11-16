package com.kalinchak.smart_parking_system.service;

import com.kalinchak.smart_parking_system.model.dto.ParkingLotDto;

public interface ParkingLotService {

    ParkingLotDto addParkingLot(final ParkingLotDto lotDto);
    void removeParkingLot(final long lotId);
}
