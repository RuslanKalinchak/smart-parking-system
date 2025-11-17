package com.kalinchak.smart_parking_system.service.impl;

import com.kalinchak.smart_parking_system.model.*;
import com.kalinchak.smart_parking_system.model.dto.CheckOutTicketDto;
import com.kalinchak.smart_parking_system.model.entity.CheckInTicket;
import com.kalinchak.smart_parking_system.model.entity.CheckOutTicket;
import com.kalinchak.smart_parking_system.model.entity.ParkingSlot;
import com.kalinchak.smart_parking_system.repository.CheckInTicketRepository;
import com.kalinchak.smart_parking_system.repository.CheckOutTicketRepository;
import com.kalinchak.smart_parking_system.repository.ParkingSlotRepository;
import com.kalinchak.smart_parking_system.service.CheckOutService;
import com.kalinchak.smart_parking_system.service.ParkingFeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CheckOutServiceImpl implements CheckOutService {

    private final CheckInTicketRepository checkInTicketRepository;
    private final CheckOutTicketRepository checkOutTicketRepository;
    private final ParkingSlotRepository parkingSlotRepository;
    private final ParkingFeeService parkingFeeService;

    @Override
    @Transactional
    public CheckOutTicketDto checkOut(final String licensePlate) {

        CheckInTicket checkInTicket = checkInTicketRepository
                .findByVehicle_LicensePlateAndStatus(licensePlate, CheckInStatus.ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("Active ticket not found"));

        ParkingSlot parkingSlot = checkInTicket.getParkingSlot();
        parkingSlot.setStatus(SlotStatus.AVAILABLE);
        ParkingSlot savedParkingSlot = parkingSlotRepository.save(parkingSlot);

        LocalDateTime exitTime = LocalDateTime.now();
        long durationMinutes = Duration.between(checkInTicket.getEntryTime(), exitTime).toMinutes();
        BigDecimal fee = parkingFeeService.calculateFee(checkInTicket.getVehicle().getVehicleType(), durationMinutes);

        checkInTicket.setStatus(CheckInStatus.INACTIVE);
        checkInTicket.setParkingSlot(savedParkingSlot);
        CheckInTicket savedCheckInTicket = checkInTicketRepository.save(checkInTicket);

        CheckOutTicket checkOutTicket = CheckOutTicket.builder()
                .checkInTicket(savedCheckInTicket)
                .exitTime(exitTime)
                .fee(fee)
                .build();

        return new CheckOutTicketDto(checkOutTicketRepository.save(checkOutTicket));
    }
}
