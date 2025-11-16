package com.kalinchak.smart_parking_system.service;

import com.kalinchak.smart_parking_system.model.*;
import com.kalinchak.smart_parking_system.model.dto.CheckInTicketDto;
import com.kalinchak.smart_parking_system.model.dto.VehicleDto;
import com.kalinchak.smart_parking_system.repository.CheckInTicketRepository;
import com.kalinchak.smart_parking_system.repository.ParkingSlotRepository;
import com.kalinchak.smart_parking_system.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CheckInService {

    private final VehicleRepository vehicleRepository;
    private final ParkingSlotRepository parkingSlotRepository;
    private final CheckInTicketRepository checkInTicketRepository;
    private final ParkingSlotService parkingSlotService;

    @Transactional
    public CheckInTicketDto checkIn(final VehicleDto vehicleDto) {

        Vehicle vehicle = vehicleRepository.findById(vehicleDto.licensePlate())
                .orElseGet(() -> vehicleRepository.save(new Vehicle(vehicleDto)));

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
}
