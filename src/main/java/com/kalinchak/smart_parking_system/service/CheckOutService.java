package com.kalinchak.smart_parking_system.service;

import com.kalinchak.smart_parking_system.model.dto.CheckOutTicketDto;

public interface CheckOutService {

    CheckOutTicketDto checkOut(final String licensePlate);
}
