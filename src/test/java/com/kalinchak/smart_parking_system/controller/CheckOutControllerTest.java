package com.kalinchak.smart_parking_system.controller;

import com.kalinchak.smart_parking_system.exception_handler.GlobalExceptionHandler;
import com.kalinchak.smart_parking_system.model.dto.CheckOutTicketDto;
import com.kalinchak.smart_parking_system.service.CheckOutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CheckOutControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CheckOutService checkOutService;

    @BeforeEach
    void setUp() {
        CheckOutController checkOutController = new CheckOutController(checkOutService);
        mockMvc = MockMvcBuilders.standaloneSetup(checkOutController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void givenValidCheckIn_whenVehicleCheckOut_thenSuccessReturnCheckOutTicket() throws Exception {
        String licensePlate = "AT7254UB";
        CheckOutTicketDto checkOutTicket = new CheckOutTicketDto(
                LocalDateTime.parse("2025-11-16T10:00:00"),
                LocalDateTime.parse("2025-11-16T13:45:30"),
                "3 hours 45 minutes 30 seconds",
                new BigDecimal("3.76")
        );

        when(checkOutService.checkOut(licensePlate)).thenReturn(checkOutTicket);

        //When
        mockMvc.perform(post("/api/check-out/%s".formatted(licensePlate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entryTime").exists())
                .andExpect(jsonPath("$.exitTime").exists())
                .andExpect(jsonPath("$.totalDuration").value("3 hours 45 minutes 30 seconds"))
                .andExpect(jsonPath("$.fee").value(3.76));

        //Then
        verify(checkOutService, times(1)).checkOut(licensePlate);
    }
}
