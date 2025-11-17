package com.kalinchak.smart_parking_system.service.impl;

import com.kalinchak.smart_parking_system.mapper.ParkingSlotMapper;
import com.kalinchak.smart_parking_system.model.*;
import com.kalinchak.smart_parking_system.model.dto.ParkingSlotDto;
import com.kalinchak.smart_parking_system.repository.ParkingLevelRepository;
import com.kalinchak.smart_parking_system.repository.ParkingLotRepository;
import com.kalinchak.smart_parking_system.repository.ParkingSlotRepository;
import com.kalinchak.smart_parking_system.service.ParkingSlotService;
import com.kalinchak.smart_parking_system.util.SlotCompatibilityConvertorUtils;
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
    private final ParkingSlotMapper parkingSlotMapper;

    @Override
    @Transactional(readOnly = true)
    public ParkingSlot findAvailableParkingSlot(final VehicleType vehicleType) {
        return parkingSlotRepository.findByTypeInAndStatus(SlotCompatibilityConvertorUtils.getCompatibleTypes(vehicleType), SlotStatus.AVAILABLE).stream()
                .min(Comparator.comparingInt(parkingSlot -> parkingSlot.getType().getSizePriority()))
                .orElseThrow(() -> new IllegalStateException("No available parking slots"));
    }

    @Override
    @Transactional
    public ParkingSlotDto addParkingSlot(final long lotId, final ParkingSlotDto slotDto) {
        checkParkingLot(lotId);

        ParkingLevel level = parkingLevelRepository.findByLevelNumberAndParkingLotId(slotDto.levelNumber(), lotId)
                .orElseThrow(() -> new IllegalArgumentException("Parking level %d for lot %d not found".formatted(slotDto.levelNumber(), lotId)));

        ParkingSlot slot = parkingSlotMapper.fromDto(slotDto, level);

        return new ParkingSlotDto(parkingSlotRepository.save(slot));
    }

    @Override
    @Transactional
    public void removeParkingSlot(final long slotId) {
        checkParkingSlot(slotId);
        parkingSlotRepository.deleteById(slotId);
    }

    private void checkParkingLot(final long lotId) {
        parkingLotRepository.findById(lotId).orElseThrow(() -> new IllegalArgumentException("Parking lot %d not found".formatted(lotId)));
    }

    private void checkParkingSlot(final long slotId) {
        parkingSlotRepository.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Parking slot %d not found".formatted(slotId)));
    }
}
