package com.kalinchak.smart_parking_system.controller;

import com.kalinchak.smart_parking_system.model.dto.ParkingLotDto;
import com.kalinchak.smart_parking_system.service.ParkingLotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/lots")
@RequiredArgsConstructor
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    @PostMapping("/add-lot")
    public ResponseEntity<ParkingLotDto> addParkingLot(
            @RequestBody ParkingLotDto lot) {
        ParkingLotDto savedLol = parkingLotService.addParkingLot(lot);
        return ResponseEntity.ok(savedLol);
    }

    @DeleteMapping("/remove-lot/{lotId}")
    public ResponseEntity<Void> removeParkingLot(@PathVariable Long lotId) {
        parkingLotService.removeParkingLot(lotId);
        return ResponseEntity.ok().build();
    }
}
