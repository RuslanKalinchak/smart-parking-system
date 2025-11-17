package com.kalinchak.smart_parking_system.factory;

import com.kalinchak.smart_parking_system.model.dto.ParkingLevelDto;

public class ParkingLevelDtoFactory {

    public static ParkingLevelDto getParkingLevelInstance(final int levelNumber) {

        return new ParkingLevelDto(
                null,
                levelNumber,
                ParkingSlotDtoFactory.getParkingSlotInstances(levelNumber));
    }
}
