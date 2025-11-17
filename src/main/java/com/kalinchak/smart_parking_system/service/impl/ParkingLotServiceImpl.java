package com.kalinchak.smart_parking_system.service.impl;

import com.kalinchak.smart_parking_system.mapper.ParkingLotMapper;
import com.kalinchak.smart_parking_system.model.ParkingLot;
import com.kalinchak.smart_parking_system.model.dto.ParkingLotDto;
import com.kalinchak.smart_parking_system.repository.ParkingLotRepository;
import com.kalinchak.smart_parking_system.service.ParkingLotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParkingLotServiceImpl implements ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;
    private final ParkingLotMapper parkingLotMapper;

    @Override
    @Transactional
    public ParkingLotDto addParkingLot(final ParkingLotDto lotDto) {
        ParkingLot parkingLot = parkingLotMapper.fromDto(lotDto);
        return new ParkingLotDto(parkingLotRepository.save(parkingLot));
    }

    @Override
    @Transactional
    public void removeParkingLot(final long lotId) {
        parkingLotRepository.delete(findParkingLot(lotId));
    }

    private ParkingLot findParkingLot(final long lotId) {
        return parkingLotRepository.findById(lotId).orElseThrow(() -> new IllegalArgumentException("Parking lot %d not found".formatted(lotId)));
    }
}
