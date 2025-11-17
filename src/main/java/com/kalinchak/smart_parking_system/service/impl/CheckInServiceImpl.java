package com.kalinchak.smart_parking_system.service.impl;

import com.kalinchak.smart_parking_system.model.*;
import com.kalinchak.smart_parking_system.model.dto.CheckInTicketDto;
import com.kalinchak.smart_parking_system.model.dto.VehicleDto;
import com.kalinchak.smart_parking_system.model.entity.CheckInTicket;
import com.kalinchak.smart_parking_system.model.entity.ParkingSlot;
import com.kalinchak.smart_parking_system.model.entity.Vehicle;
import com.kalinchak.smart_parking_system.repository.CheckInTicketRepository;
import com.kalinchak.smart_parking_system.repository.ParkingSlotRepository;
import com.kalinchak.smart_parking_system.repository.VehicleRepository;
import com.kalinchak.smart_parking_system.service.CheckInService;
import com.kalinchak.smart_parking_system.service.ParkingSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckInServiceImpl implements CheckInService {

    private final VehicleRepository vehicleRepository;
    private final ParkingSlotRepository parkingSlotRepository;
    private final CheckInTicketRepository checkInTicketRepository;
    private final ParkingSlotService parkingSlotService;

    @Override
    @Transactional
    public CheckInTicketDto checkIn(final VehicleDto vehicleDto) {

        Vehicle vehicle = vehicleRepository.findById(vehicleDto.licensePlate())
                .orElseGet(() -> vehicleRepository.save(new Vehicle(vehicleDto.licensePlate(), vehicleDto.vehicleType())));

        ParkingSlot availableParkingSlot = parkingSlotService.findAvailableParkingSlot(vehicle.getVehicleType());
        availableParkingSlot.setStatus(SlotStatus.OCCUPIED);
        ParkingSlot savedParkingSlot = parkingSlotRepository.save(availableParkingSlot);

        CheckInTicket checkInTicket = CheckInTicket.builder()
                .vehicle(vehicle)
                .parkingSlot(savedParkingSlot)
                .entryTime(LocalDateTime.now())
                .status(CheckInStatus.ACTIVE)
                .build();

        return new CheckInTicketDto(checkInTicketRepository.save(checkInTicket));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CheckInTicketDto> findAllActiveCheckIns() {
        return checkInTicketRepository.findAllByStatus(CheckInStatus.ACTIVE)
                .stream()
                .map(CheckInTicketDto::new)
                .toList();
    }
}
