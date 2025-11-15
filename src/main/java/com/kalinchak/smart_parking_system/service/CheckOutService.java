package com.kalinchak.smart_parking_system.service;

import com.kalinchak.smart_parking_system.model.*;
import com.kalinchak.smart_parking_system.model.dto.CheckOutTicketDto;
import com.kalinchak.smart_parking_system.repository.CheckInTicketRepository;
import com.kalinchak.smart_parking_system.repository.CheckOutTicketRepository;
import com.kalinchak.smart_parking_system.repository.ParkingSlotRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CheckOutService {

    private final CheckInTicketRepository checkInTicketRepository;
    private final CheckOutTicketRepository checkOutTicketRepository;
    private final ParkingSlotRepository parkingSlotRepository;
    private final ParkingFeeService parkingFeeService;

    @Transactional
    public CheckOutTicketDto checkOut(final String licensePlate) {

        CheckInTicket checkInTicket = checkInTicketRepository
                .findByVehicle_LicensePlateAndStatus(licensePlate, CheckInStatus.ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("Active ticket not found"));

        ParkingSlot parkingSlot = checkInTicket.getParkingSlot();
        parkingSlot.setStatus(SlotStatus.AVAILABLE);
        parkingSlotRepository.save(parkingSlot);

        LocalDateTime exitTime = LocalDateTime.now();
        long durationMinutes = Duration.between(checkInTicket.getEntryTime(), exitTime).toMinutes();
        BigDecimal fee = parkingFeeService.calculateFee(checkInTicket.getVehicle().getVehicleType(), durationMinutes);

        checkInTicket.setStatus(CheckInStatus.INACTIVE);
        CheckInTicket savedCheckInTicket = checkInTicketRepository.save(checkInTicket);

        CheckOutTicket checkOutTicket = CheckOutTicket.builder()
                .checkInTicket(savedCheckInTicket)
                .exitTime(exitTime)
                .fee(fee)
                .build();

        return new CheckOutTicketDto(checkOutTicketRepository.save(checkOutTicket));
    }
}
