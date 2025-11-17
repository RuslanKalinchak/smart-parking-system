package com.kalinchak.smart_parking_system.controller;

import com.kalinchak.smart_parking_system.model.dto.ParkingLevelDto;
import com.kalinchak.smart_parking_system.service.ParkingLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/levels")
@RequiredArgsConstructor
public class ParkingLevelController {

    private final ParkingLevelService parkingLevelService;

    @PostMapping("/add-level")
    public ResponseEntity<ParkingLevelDto> addParkingLevel(
            @RequestParam long lotId,
            @RequestBody ParkingLevelDto level) {
        ParkingLevelDto savedLevel = parkingLevelService.addParkingLevel(lotId, level);
        return ResponseEntity.ok(savedLevel);
    }

    @DeleteMapping("/remove-level/{levelId}")
    public ResponseEntity<Void> removeParkingLevel(@PathVariable long levelId) {
        parkingLevelService.removeParkingLevel(levelId);
        return ResponseEntity.ok().build();
    }
}
