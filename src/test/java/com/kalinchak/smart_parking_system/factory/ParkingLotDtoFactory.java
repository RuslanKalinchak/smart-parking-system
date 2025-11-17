package com.kalinchak.smart_parking_system.factory;

import com.kalinchak.smart_parking_system.model.dto.ParkingLotDto;

import java.util.List;

public class ParkingLotDtoFactory {

    public static ParkingLotDto getParkingLotInstance(final int levelNumber) {

        return new ParkingLotDto(
                null,
                "Main Parking Lot C",
                List.of(ParkingLevelDtoFactory.getParkingLevelInstance(levelNumber))
        );
    }
}
