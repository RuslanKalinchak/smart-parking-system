package com.kalinchak.smart_parking_system.service;

import com.kalinchak.smart_parking_system.model.dto.ParkingLevelDto;

public interface ParkingLevelService {

    ParkingLevelDto addParkingLevel(final long lotId, final ParkingLevelDto levelDto);
    void removeParkingLevel(final long levelId);
}
