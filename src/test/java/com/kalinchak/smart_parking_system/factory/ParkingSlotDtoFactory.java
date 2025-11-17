package com.kalinchak.smart_parking_system.factory;

import com.kalinchak.smart_parking_system.model.SlotType;
import com.kalinchak.smart_parking_system.model.dto.ParkingSlotDto;

import java.util.List;

public class ParkingSlotDtoFactory {

    public static ParkingSlotDto getParkingSlotInstanceMotorcycle(final int levelNumber) {
        return new ParkingSlotDto(
                null,
                "TEST-MOTO-1",
                SlotType.MOTORCYCLE,
                levelNumber
        );
    }

    public static ParkingSlotDto getParkingSlotInstanceCompact(final int levelNumber) {
        return new ParkingSlotDto(
                null,
                "TEST-COMPACT-1",
                SlotType.COMPACT,
                levelNumber
        );
    }

    public static ParkingSlotDto getParkingSlotInstanceLarge(final int levelNumber) {
        return new ParkingSlotDto(
                null,
                "TEST-LARGE-1",
                SlotType.LARGE,
                levelNumber
        );
    }

    public static List<ParkingSlotDto> getParkingSlotInstances(final int levelNumber) {
        return List.of(
                getParkingSlotInstanceMotorcycle(levelNumber),
                getParkingSlotInstanceCompact(levelNumber),
                getParkingSlotInstanceLarge(levelNumber)
        );
    }
}
