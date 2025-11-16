package com.kalinchak.smart_parking_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kalinchak.smart_parking_system.exception_handler.GlobalExceptionHandler;
import com.kalinchak.smart_parking_system.model.SlotType;
import com.kalinchak.smart_parking_system.model.VehicleType;
import com.kalinchak.smart_parking_system.model.dto.CheckInTicketDto;
import com.kalinchak.smart_parking_system.model.dto.ParkingSlotDto;
import com.kalinchak.smart_parking_system.model.dto.VehicleDto;
import com.kalinchak.smart_parking_system.service.CheckInService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CheckInControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CheckInService checkInService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        CheckInController checkInController = new CheckInController(checkInService);
        mockMvc = MockMvcBuilders.standaloneSetup(checkInController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void givenValidVehicle_whenVehicleCheckIn_thenSuccessReturnCheckInTicket() throws Exception {
        //Given
        VehicleDto vehicle = new VehicleDto("AT7254UB", VehicleType.CAR);
        CheckInTicketDto checkInTicket = new CheckInTicketDto(vehicle, LocalDateTime.now(),
                new ParkingSlotDto(12L, "L3-S2", SlotType.COMPACT, 3));

        when(checkInService.checkIn(vehicle)).thenReturn(checkInTicket);

        //When
        mockMvc.perform(post("/api/check-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicle)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicleDto.licensePlate").value("AT7254UB"))
                .andExpect(jsonPath("$.vehicleDto.vehicleType").value("CAR"))
                .andExpect(jsonPath("$.parkingSlotDto.slotCode").value("L3-S2"))
                .andExpect(jsonPath("$.parkingSlotDto.type").value("COMPACT"))
                .andExpect(jsonPath("$.entryTime").exists());

        //Then
        verify(checkInService, times(1)).checkIn(vehicle);
    }
}
