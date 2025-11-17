package com.kalinchak.smart_parking_system.service;

import com.kalinchak.smart_parking_system.model.CheckInStatus;
import com.kalinchak.smart_parking_system.model.SlotStatus;
import com.kalinchak.smart_parking_system.model.SlotType;
import com.kalinchak.smart_parking_system.model.VehicleType;
import com.kalinchak.smart_parking_system.model.dto.CheckInTicketDto;
import com.kalinchak.smart_parking_system.model.dto.VehicleDto;
import com.kalinchak.smart_parking_system.model.entity.CheckInTicket;
import com.kalinchak.smart_parking_system.model.entity.ParkingLevel;
import com.kalinchak.smart_parking_system.model.entity.ParkingSlot;
import com.kalinchak.smart_parking_system.model.entity.Vehicle;
import com.kalinchak.smart_parking_system.repository.CheckInTicketRepository;
import com.kalinchak.smart_parking_system.repository.ParkingSlotRepository;
import com.kalinchak.smart_parking_system.repository.VehicleRepository;
import com.kalinchak.smart_parking_system.service.impl.CheckInServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckInServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private ParkingSlotRepository parkingSlotRepository;

    @Mock
    private CheckInTicketRepository checkInTicketRepository;

    @Mock
    private ParkingSlotService parkingSlotService;

    @InjectMocks
    private CheckInServiceImpl checkInService;

    @Test
    void givenNewVehicle_whenCreateCheckIn_thenCreateVehicleAndAssignSlot() {

        //Given
        VehicleDto vehicleDto = new VehicleDto("AA1111BB", VehicleType.CAR);

        Vehicle vehicle = new Vehicle(vehicleDto.licensePlate(), vehicleDto.vehicleType());
        ParkingSlot availableSlot = ParkingSlot.builder()
                .id(10L)
                .slotCode("L1-S1")
                .type(SlotType.COMPACT)
                .status(SlotStatus.AVAILABLE)
                .build();

        ParkingSlot occupiedSlot = ParkingSlot.builder()
                .id(10L)
                .slotCode("L1-S1")
                .type(SlotType.COMPACT)
                .status(SlotStatus.OCCUPIED)
                .parkingLevel(new ParkingLevel())
                .build();

        CheckInTicket savedTicket = CheckInTicket.builder()
                .id(50L)
                .vehicle(vehicle)
                .parkingSlot(occupiedSlot)
                .entryTime(LocalDateTime.now())
                .status(CheckInStatus.ACTIVE)
                .build();

        when(vehicleRepository.findById("AA1111BB")).thenReturn(Optional.empty());
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);
        when(parkingSlotService.findAvailableParkingSlot(VehicleType.CAR)).thenReturn(availableSlot);
        when(parkingSlotRepository.save(any(ParkingSlot.class))).thenReturn(occupiedSlot);
        when(checkInTicketRepository.save(any(CheckInTicket.class))).thenReturn(savedTicket);

        //When
        CheckInTicketDto result = checkInService.checkIn(vehicleDto);

        //Then
        assertThat(result.vehicleDto()).isEqualTo(vehicleDto);
        assertThat(result.entryTime()).isNotNull();
        assertThat(result.parkingSlotDto().id()).isEqualTo(occupiedSlot.getId());

        InOrder inOrder = inOrder(vehicleRepository, parkingSlotService, parkingSlotRepository, checkInTicketRepository);
        inOrder.verify(vehicleRepository).findById("AA1111BB");
        inOrder.verify(vehicleRepository).save(any(Vehicle.class));
        inOrder.verify(parkingSlotService).findAvailableParkingSlot(VehicleType.CAR);
        inOrder.verify(parkingSlotRepository).save(any(ParkingSlot.class));
        inOrder.verify(checkInTicketRepository).save(any(CheckInTicket.class));
    }

    @Test
    void givenExistingVehicle_whenCreateCheckIn_thenReuseExistingVehicleAndCreateCheckInTicket() {
        //Given
        VehicleDto vehicleDto = new VehicleDto("BB2222CC", VehicleType.TRUCK);

        Vehicle existingVehicle = new Vehicle(vehicleDto.licensePlate(), vehicleDto.vehicleType());
        ParkingSlot parkingSlot = ParkingSlot.builder()
                .id(20L)
                .slotCode("L1-S2")
                .type(SlotType.LARGE)
                .parkingLevel(new ParkingLevel())
                .build();

        CheckInTicket checkInTicket = CheckInTicket.builder()
                .id(50L)
                .vehicle(existingVehicle)
                .parkingSlot(parkingSlot)
                .entryTime(LocalDateTime.now())
                .status(CheckInStatus.ACTIVE)
                .build();

        when(vehicleRepository.findById("BB2222CC")).thenReturn(Optional.of(existingVehicle));
        when(parkingSlotService.findAvailableParkingSlot(VehicleType.TRUCK)).thenReturn(parkingSlot);
        when(parkingSlotRepository.save(any())).thenReturn(parkingSlot);
        when(checkInTicketRepository.save(any())).thenReturn(checkInTicket);

        //When
        checkInService.checkIn(vehicleDto);

        //Then
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    void givenValidCheckIns_whenFindAllActiveCheckIns_thenReturnMappedDtos() {
        //Given
        CheckInTicket ticket1 = CheckInTicket.builder().id(1L)
                .vehicle(new Vehicle())
                .parkingSlot(ParkingSlot.builder().parkingLevel(new ParkingLevel()).build())
                .status(CheckInStatus.ACTIVE).build();
        CheckInTicket ticket2 = CheckInTicket.builder().id(2L)
                .vehicle(new Vehicle())
                .parkingSlot(ParkingSlot.builder().parkingLevel(new ParkingLevel()).build())
                .status(CheckInStatus.ACTIVE).build();

        List<CheckInTicket> checkInTickets = List.of(ticket1, ticket2);

        when(checkInTicketRepository.findAllByStatus(CheckInStatus.ACTIVE))
                .thenReturn(checkInTickets);

        //When
        List<CheckInTicketDto> result = checkInService.findAllActiveCheckIns();

        //Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);

        verify(checkInTicketRepository).findAllByStatus(CheckInStatus.ACTIVE);
    }
}

