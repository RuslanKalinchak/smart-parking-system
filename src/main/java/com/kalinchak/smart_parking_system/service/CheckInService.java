package com.kalinchak.smart_parking_system.service;

import com.kalinchak.smart_parking_system.model.*;
import com.kalinchak.smart_parking_system.model.dto.CheckInTicketDto;
import com.kalinchak.smart_parking_system.model.dto.VehicleDto;
import com.kalinchak.smart_parking_system.repository.CheckInTicketRepository;
import com.kalinchak.smart_parking_system.repository.ParkingSlotRepository;
import com.kalinchak.smart_parking_system.repository.VehicleRepository;
import com.kalinchak.smart_parking_system.util.SlotCompatibilityConvertorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class CheckInService {

    private final VehicleRepository vehicleRepository;
    private final ParkingSlotRepository parkingSlotRepository;
    private final CheckInTicketRepository checkInTicketRepository;

    public CheckInTicketDto checkIn(final VehicleDto vehicleDto) {

        Vehicle vehicle = vehicleRepository.findById(vehicleDto.licensePlate())
                .orElseGet(() -> vehicleRepository.save(new Vehicle(vehicleDto)));

        ParkingSlot slot = parkingSlotRepository.findByTypeInAndStatus(SlotCompatibilityConvertorUtil.getCompatibleTypes(vehicle.getVehicleType()), SlotStatus.AVAILABLE).stream()
                .min(Comparator.comparingInt(parkingSlot -> parkingSlot.getType().getSizePriority()))
                .orElseThrow(() -> new IllegalStateException("No available parking slots"));

        slot.setStatus(SlotStatus.OCCUPIED);
        ParkingSlot savedParkingSlot = parkingSlotRepository.save(slot);

        CheckInTicket checkInTicket = CheckInTicket.builder()
                .vehicle(vehicle)
                .parkingSlot(savedParkingSlot)
                .entryTime(LocalDateTime.now())
                .status(CheckInStatus.ACTIVE)
                .build();

       return new CheckInTicketDto(checkInTicketRepository.save(checkInTicket));
    }
}
