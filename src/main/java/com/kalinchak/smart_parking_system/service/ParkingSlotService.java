package com.kalinchak.smart_parking_system.service;

import com.kalinchak.smart_parking_system.model.entity.ParkingSlot;
import com.kalinchak.smart_parking_system.model.VehicleType;
import com.kalinchak.smart_parking_system.model.dto.ParkingSlotDto;

public interface ParkingSlotService {

    ParkingSlot findAvailableParkingSlot(final VehicleType vehicleType);
    ParkingSlotDto addParkingSlot(final long lotId, final ParkingSlotDto slotDto);
    void removeParkingSlot(final long slotId);
}
