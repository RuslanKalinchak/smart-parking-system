package com.kalinchak.smart_parking_system.controller;

import com.kalinchak.smart_parking_system.model.dto.CheckOutTicketDto;
import com.kalinchak.smart_parking_system.service.CheckOutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/check-out")
@RequiredArgsConstructor
public class CheckOutController {

    private final CheckOutService checkOutService;

    @PostMapping("/{licensePlate}")
    public ResponseEntity<CheckOutTicketDto> checkOut(@PathVariable String licensePlate) {
        CheckOutTicketDto checkOutTicket = checkOutService.checkOut(licensePlate);
        return ResponseEntity.ok(checkOutTicket);
    }
}
