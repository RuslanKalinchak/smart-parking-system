package com.kalinchak.smart_parking_system.service;

import com.kalinchak.smart_parking_system.model.ParkingSlot;
import com.kalinchak.smart_parking_system.model.VehicleType;
import com.kalinchak.smart_parking_system.model.dto.ParkingSlotDto;

public interface ParkingSlotService {

    ParkingSlot findAvailableParkingSlot(final VehicleType vehicleType);
    ParkingSlotDto addParkingSlot(final long parkingLotId, final ParkingSlotDto parkingSlotDto);
    void removeParkingSlot(final long slotId);
}
