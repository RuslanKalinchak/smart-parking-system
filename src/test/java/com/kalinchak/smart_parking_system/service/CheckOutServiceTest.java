package com.kalinchak.smart_parking_system.service;

import com.kalinchak.smart_parking_system.model.CheckInStatus;
import com.kalinchak.smart_parking_system.model.SlotStatus;
import com.kalinchak.smart_parking_system.model.SlotType;
import com.kalinchak.smart_parking_system.model.VehicleType;
import com.kalinchak.smart_parking_system.model.dto.CheckOutTicketDto;
import com.kalinchak.smart_parking_system.model.entity.CheckInTicket;
import com.kalinchak.smart_parking_system.model.entity.CheckOutTicket;
import com.kalinchak.smart_parking_system.model.entity.ParkingSlot;
import com.kalinchak.smart_parking_system.model.entity.Vehicle;
import com.kalinchak.smart_parking_system.repository.CheckInTicketRepository;
import com.kalinchak.smart_parking_system.repository.CheckOutTicketRepository;
import com.kalinchak.smart_parking_system.repository.ParkingSlotRepository;
import com.kalinchak.smart_parking_system.service.impl.CheckOutServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckOutServiceTest {

    @Mock
    private CheckInTicketRepository checkInTicketRepository;

    @Mock
    private CheckOutTicketRepository checkOutTicketRepository;

    @Mock
    private ParkingSlotRepository parkingSlotRepository;

    @Mock
    private ParkingFeeService parkingFeeService;

    @InjectMocks
    private CheckOutServiceImpl checkOutService;

    @Test
    void givenActiveCheckInTicket_whenCheckOut_thenReturnsCheckOutTicketDto() {
        //Given
        String plate = "AT7254UB";

        Vehicle vehicle = new Vehicle(plate, VehicleType.CAR);

        ParkingSlot slot = ParkingSlot.builder()
                .slotCode("L1-S1")
                .type(SlotType.COMPACT)
                .status(SlotStatus.OCCUPIED)
                .build();

        LocalDateTime entryTime = LocalDateTime.now().minusMinutes(120);

        CheckInTicket activeTicket = CheckInTicket.builder()
                .vehicle(vehicle)
                .parkingSlot(slot)
                .entryTime(entryTime)
                .status(CheckInStatus.ACTIVE)
                .build();

        BigDecimal expectedFee = BigDecimal.valueOf(4);

        when(checkInTicketRepository.findByVehicle_LicensePlateAndStatus(plate, CheckInStatus.ACTIVE))
                .thenReturn(Optional.of(activeTicket));

        when(parkingSlotRepository.save(slot)).thenReturn(slot);
        when(parkingFeeService.calculateFee(VehicleType.CAR, 120)).thenReturn(expectedFee);
        when(checkInTicketRepository.save(any(CheckInTicket.class))).thenReturn(activeTicket);

        CheckOutTicket savedCheckout = CheckOutTicket.builder()
                .checkInTicket(activeTicket)
                .exitTime(LocalDateTime.now())
                .fee(expectedFee)
                .build();

        when(checkOutTicketRepository.save(any(CheckOutTicket.class)))
                .thenReturn(savedCheckout);

        //When
        CheckOutTicketDto result = checkOutService.checkOut(plate);

        //Then
        assertThat(result).isNotNull();
        assertThat(result.fee()).isEqualTo(expectedFee);

        verify(checkInTicketRepository).findByVehicle_LicensePlateAndStatus(plate, CheckInStatus.ACTIVE);
        verify(parkingSlotRepository).save(slot);
        verify(parkingFeeService).calculateFee(VehicleType.CAR, 120);
        verify(checkOutTicketRepository).save(any(CheckOutTicket.class));
    }

    @Test
    void givenNonExistentCar_whenCheckOut_thenThrowsException() {
        //Given
        when(checkInTicketRepository
                .findByVehicle_LicensePlateAndStatus("NOTFOUND", CheckInStatus.ACTIVE))
                .thenReturn(Optional.empty());

        //When
        assertThatThrownBy(() -> checkOutService.checkOut("NOTFOUND"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Active ticket not found");

        //Then
        verify(checkInTicketRepository)
                .findByVehicle_LicensePlateAndStatus("NOTFOUND", CheckInStatus.ACTIVE);
        verifyNoMoreInteractions(parkingSlotRepository, parkingFeeService, checkOutTicketRepository);
    }
}
