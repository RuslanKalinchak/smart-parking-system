package com.kalinchak.smart_parking_system.service;

import com.kalinchak.smart_parking_system.model.ParkingSlot;
import com.kalinchak.smart_parking_system.model.SlotStatus;
import com.kalinchak.smart_parking_system.model.VehicleType;
import com.kalinchak.smart_parking_system.repository.ParkingSlotRepository;
import com.kalinchak.smart_parking_system.util.SlotCompatibilityConvertorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class ParkingSlotService {

    private final ParkingSlotRepository parkingSlotRepository;

    public ParkingSlot findAvailableParkingSlot(final VehicleType vehicleType) {
        return parkingSlotRepository.findByTypeInAndStatus(SlotCompatibilityConvertorUtil.getCompatibleTypes(vehicleType), SlotStatus.AVAILABLE).stream()
                .min(Comparator.comparingInt(parkingSlot -> parkingSlot.getType().getSizePriority()))
                .orElseThrow(() -> new IllegalStateException("No available parking slots"));
    }
}
