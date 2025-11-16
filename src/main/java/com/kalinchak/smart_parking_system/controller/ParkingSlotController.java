package com.kalinchak.smart_parking_system.controller;

import com.kalinchak.smart_parking_system.model.dto.ParkingSlotDto;
import com.kalinchak.smart_parking_system.service.ParkingSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class ParkingSlotController {

    private final ParkingSlotService parkingSlotService;

    @PostMapping("/lots/{lotId}/add-slot")
    public ResponseEntity<ParkingSlotDto> addParkingSlot(
            @PathVariable Long lotId,
            @RequestBody ParkingSlotDto slot) {
        ParkingSlotDto savedSlot = parkingSlotService.addParkingSlot(lotId, slot);
        return ResponseEntity.ok(savedSlot);
    }

    @DeleteMapping("/slots/remove-slot/{slotId}")
    public ResponseEntity<Void> removeParkingSlot(@PathVariable Long slotId) {
        parkingSlotService.removeParkingSlot(slotId);
        return ResponseEntity.ok().build();
    }
}
