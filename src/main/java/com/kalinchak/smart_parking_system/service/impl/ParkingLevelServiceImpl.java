package com.kalinchak.smart_parking_system.service.impl;

import com.kalinchak.smart_parking_system.model.ParkingLevel;
import com.kalinchak.smart_parking_system.model.ParkingLot;
import com.kalinchak.smart_parking_system.model.ParkingSlot;
import com.kalinchak.smart_parking_system.model.SlotStatus;
import com.kalinchak.smart_parking_system.model.dto.ParkingLevelDto;
import com.kalinchak.smart_parking_system.repository.ParkingLevelRepository;
import com.kalinchak.smart_parking_system.repository.ParkingLotRepository;
import com.kalinchak.smart_parking_system.service.ParkingLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingLevelServiceImpl implements ParkingLevelService {

    private final ParkingLotRepository parkingLotRepository;
    private final ParkingLevelRepository parkingLevelRepository;

    @Override
    @Transactional
    public ParkingLevelDto addParkingLevel(long lotId, ParkingLevelDto levelDto) {
        ParkingLevel parkingLevel = ParkingLevel.builder()
                .levelNumber(levelDto.levelNumber())
                .parkingLot(findParkingLot(lotId))
                .build();

        List<ParkingSlot> slots = levelDto.slots().stream().map(slotDto -> ParkingSlot.builder()
                        .slotCode(slotDto.slotCode())
                        .type(slotDto.type())
                        .status(SlotStatus.AVAILABLE)
                        .parkingLevel(parkingLevel)
                        .build())
                .toList();

        parkingLevel.setSlots(slots);

        return new ParkingLevelDto(parkingLevelRepository.save(parkingLevel));
    }

    @Override
    @Transactional
    public void removeParkingLevel(long levelId) {
        parkingLevelRepository.delete(findParkingLevel(levelId));
    }

    private ParkingLot findParkingLot(final long lotId) {
        return parkingLotRepository.findById(lotId).orElseThrow(() -> new IllegalArgumentException("Parking lot %d not found".formatted(lotId)));
    }

    private ParkingLevel findParkingLevel(final long levelId) {
        return parkingLevelRepository.findById(levelId).orElseThrow(() -> new IllegalArgumentException("Parking level %d not found".formatted(levelId)));
    }
}
