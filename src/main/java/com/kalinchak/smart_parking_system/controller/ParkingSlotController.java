package com.kalinchak.smart_parking_system.controller;

import com.kalinchak.smart_parking_system.model.dto.ParkingSlotDto;
import com.kalinchak.smart_parking_system.service.ParkingSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/slots")
@RequiredArgsConstructor
public class ParkingSlotController {

    private final ParkingSlotService parkingSlotService;

    @PostMapping("/add-slot")
    public ResponseEntity<ParkingSlotDto> addParkingSlot(
            @RequestParam Long lotId,
            @RequestBody ParkingSlotDto slot) {
        ParkingSlotDto savedSlot = parkingSlotService.addParkingSlot(lotId, slot);
        return ResponseEntity.ok(savedSlot);
    }

    @DeleteMapping("/remove-slot/{slotId}")
    public ResponseEntity<Void> removeParkingSlot(@PathVariable long slotId) {
        parkingSlotService.removeParkingSlot(slotId);
        return ResponseEntity.ok().build();
    }
}
