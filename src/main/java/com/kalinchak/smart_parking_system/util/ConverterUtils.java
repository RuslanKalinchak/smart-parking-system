package com.kalinchak.smart_parking_system.util;

import com.kalinchak.smart_parking_system.model.ParkingLevel;
import com.kalinchak.smart_parking_system.model.ParkingLot;
import com.kalinchak.smart_parking_system.model.ParkingSlot;
import com.kalinchak.smart_parking_system.model.SlotStatus;
import com.kalinchak.smart_parking_system.model.dto.ParkingLevelDto;
import com.kalinchak.smart_parking_system.model.dto.ParkingLotDto;
import com.kalinchak.smart_parking_system.model.dto.ParkingSlotDto;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ConverterUtils {

    public static ParkingSlot dtoToParkingSlot(final ParkingSlotDto slotDto, final ParkingLevel level) {
        return ParkingSlot.builder()
                .slotCode(slotDto.slotCode())
                .type(slotDto.type())
                .parkingLevel(level)
                .status(SlotStatus.AVAILABLE)
                .build();
    }

    public static ParkingLevel dtoToParkingLevel(final ParkingLevelDto levelDto, final ParkingLot lot) {
        ParkingLevel level = ParkingLevel.builder()
                .levelNumber(levelDto.levelNumber())
                .parkingLot(lot)
                .build();

        List<ParkingSlot> slots = levelDto.slots().stream().map(slotDto -> dtoToParkingSlot(slotDto, level)).toList();

        level.setSlots(slots);
        return level;
    }

    public static ParkingLot dtoToParkingLot(final ParkingLotDto lotDto) {
        ParkingLot lot = ParkingLot.builder()
                .name(lotDto.name())
                .build();

        List<ParkingLevel> parkingLevels = lotDto.parkingLevels().stream()
                .map(levelDto -> dtoToParkingLevel(levelDto, lot))
                .toList();

        lot.setParkingLevels(parkingLevels);
        return lot;
    }
}
