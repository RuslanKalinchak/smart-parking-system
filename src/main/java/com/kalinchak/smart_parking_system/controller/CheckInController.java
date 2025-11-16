package com.kalinchak.smart_parking_system.controller;

import com.kalinchak.smart_parking_system.model.dto.CheckInTicketDto;
import com.kalinchak.smart_parking_system.model.dto.VehicleDto;
import com.kalinchak.smart_parking_system.service.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/check-in")
@RequiredArgsConstructor
public class CheckInController {

    private final CheckInService checkInService;

    @PostMapping
    public ResponseEntity<CheckInTicketDto> checkIn(@RequestBody VehicleDto vehicleDto) {
        return ResponseEntity.ok(checkInService.checkIn(vehicleDto));
    }

    @GetMapping("/find-all-active-check-ins")
    public ResponseEntity<List<CheckInTicketDto>> getActiveCheckIns() {
        return ResponseEntity.ok(checkInService.findAllActiveCheckIns());
    }
}
