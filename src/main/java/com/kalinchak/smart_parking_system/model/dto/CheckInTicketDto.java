package com.kalinchak.smart_parking_system.model.dto;

import com.kalinchak.smart_parking_system.model.CheckInTicket;

import java.time.LocalDateTime;

public record CheckInTicketDto(VehicleDto vehicleDto, LocalDateTime entryTime, ParkingSlotDto parkingSlotDto) {

    public CheckInTicketDto(final CheckInTicket checkInTicket) {
        this(new VehicleDto(checkInTicket.getVehicle()), checkInTicket.getEntryTime(), new ParkingSlotDto(checkInTicket.getParkingSlot()));
    }
}
