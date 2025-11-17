package com.kalinchak.smart_parking_system.service.impl;

import com.kalinchak.smart_parking_system.mapper.ParkingLevelMapper;
import com.kalinchak.smart_parking_system.model.entity.ParkingLevel;
import com.kalinchak.smart_parking_system.model.entity.ParkingLot;
import com.kalinchak.smart_parking_system.model.dto.ParkingLevelDto;
import com.kalinchak.smart_parking_system.repository.ParkingLevelRepository;
import com.kalinchak.smart_parking_system.repository.ParkingLotRepository;
import com.kalinchak.smart_parking_system.service.ParkingLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParkingLevelServiceImpl implements ParkingLevelService {

    private final ParkingLotRepository parkingLotRepository;
    private final ParkingLevelRepository parkingLevelRepository;
    private final ParkingLevelMapper parkingLevelMapper;

    @Override
    @Transactional
    public ParkingLevelDto addParkingLevel(final long lotId, final ParkingLevelDto levelDto) {
        ParkingLevel level = parkingLevelMapper.fromDto(levelDto, findParkingLot(lotId));
        return new ParkingLevelDto(parkingLevelRepository.save(level));
    }

    @Override
    @Transactional
    public void removeParkingLevel(final long levelId) {
        parkingLevelRepository.delete(findParkingLevel(levelId));
    }

    private ParkingLot findParkingLot(final long lotId) {
        return parkingLotRepository.findById(lotId).orElseThrow(() -> new IllegalArgumentException("Parking lot %d not found".formatted(lotId)));
    }

    private ParkingLevel findParkingLevel(final long levelId) {
        return parkingLevelRepository.findById(levelId).orElseThrow(() -> new IllegalArgumentException("Parking level %d not found".formatted(levelId)));
    }
}
