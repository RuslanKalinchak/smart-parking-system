package com.kalinchak.smart_parking_system.service;

import com.kalinchak.smart_parking_system.model.dto.CheckInTicketDto;
import com.kalinchak.smart_parking_system.model.dto.VehicleDto;

import java.util.List;

public interface CheckInService {
    CheckInTicketDto checkIn(final VehicleDto vehicleDto);
    List<CheckInTicketDto> findAllActiveCheckIns();
}
