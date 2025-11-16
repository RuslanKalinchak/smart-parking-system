package com.kalinchak.smart_parking_system.service.impl;

import com.kalinchak.smart_parking_system.model.*;
import com.kalinchak.smart_parking_system.model.dto.ParkingSlotDto;
import com.kalinchak.smart_parking_system.repository.ParkingLevelRepository;
import com.kalinchak.smart_parking_system.repository.ParkingLotRepository;
import com.kalinchak.smart_parking_system.repository.ParkingSlotRepository;
import com.kalinchak.smart_parking_system.service.ParkingSlotService;
import com.kalinchak.smart_parking_system.util.SlotCompatibilityConvertorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class ParkingSlotServiceImpl implements ParkingSlotService {

    private final ParkingSlotRepository parkingSlotRepository;
    private final ParkingLevelRepository parkingLevelRepository;
    private final ParkingLotRepository parkingLotRepository;

    @Override
    @Transactional(readOnly = true)
    public ParkingSlot findAvailableParkingSlot(final VehicleType vehicleType) {
        return parkingSlotRepository.findByTypeInAndStatus(SlotCompatibilityConvertorUtil.getCompatibleTypes(vehicleType), SlotStatus.AVAILABLE).stream()
                .min(Comparator.comparingInt(parkingSlot -> parkingSlot.getType().getSizePriority()))
                .orElseThrow(() -> new IllegalStateException("No available parking slots"));
    }

    @Override
    @Transactional
    public ParkingSlotDto addParkingSlot(final long parkingLotId, final ParkingSlotDto parkingSlotDto) {
        checkParkingLot(parkingLotId);

        ParkingLevel level = parkingLevelRepository.findByLevelNumberAndParkingLotId(parkingSlotDto.levelNumber(), parkingLotId)
                .orElseThrow(() -> new IllegalArgumentException("Parking level %d for lot %d not found".formatted(parkingSlotDto.levelNumber(), parkingLotId)));

        ParkingSlot slot = ParkingSlot.builder()
                .parkingLevel(level)
                .slotCode(parkingSlotDto.slotCode())
                .type(parkingSlotDto.type())
                .parkingLevel(level)
                .status(SlotStatus.AVAILABLE)
                .build();

        return new ParkingSlotDto(parkingSlotRepository.save(slot));
    }

    @Override
    @Transactional
    public void removeParkingSlot(final long slotId) {
        checkParkingSlot(slotId);
        parkingSlotRepository.deleteById(slotId);
    }

    private void checkParkingLot(final long parkingLotId) {
        parkingLotRepository.findById(parkingLotId).orElseThrow(() -> new IllegalArgumentException("Parking lot %d not found".formatted(parkingLotId)));
    }

    private void checkParkingSlot(final long slotId) {
        parkingSlotRepository.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Parking slot %d not found".formatted(slotId)));
    }
}
