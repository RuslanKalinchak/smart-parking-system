package com.kalinchak.smart_parking_system.controller;

import com.kalinchak.smart_parking_system.controller.configuration.TestContainersConfig;
import com.kalinchak.smart_parking_system.model.ParkingSlot;
import com.kalinchak.smart_parking_system.model.SlotStatus;
import com.kalinchak.smart_parking_system.model.VehicleType;
import com.kalinchak.smart_parking_system.model.dto.CheckInTicketDto;
import com.kalinchak.smart_parking_system.model.dto.VehicleDto;
import com.kalinchak.smart_parking_system.repository.ParkingSlotRepository;
import com.kalinchak.smart_parking_system.util.SlotCompatibilityConvertorUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = TestContainersConfig.class)
@ActiveProfiles("integration-test")
class CheckInControllerIT {

    private RestClient restClient;
    @LocalServerPort
    private int port;
    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder().baseUrl("http://localhost:" + port).build();
    }

    @ParameterizedTest
    @MethodSource("provideCheckInParameters")
    @Sql(scripts = "/sql/check_in_clean_script.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void givenValidVehicle_whenVehicleCheckIn_thenSuccessReturnCheckInTicket(final VehicleDto vehicle) {
        //Given
        URI uri = UriComponentsBuilder.fromPath("/api/check-in")
                .build().toUri();

        //When
        ResponseEntity<CheckInTicketDto> response = restClient.post()
                .uri(uri)
                .body(vehicle)
                .retrieve()
                .toEntity(CheckInTicketDto.class);

        //Then
        CheckInTicketDto actualCheckInTicket = response.getBody();
        ParkingSlot actualParkingSlot = parkingSlotRepository.findBySlotCode(actualCheckInTicket.parkingSlotDto().slotCode()).orElseThrow();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(actualCheckInTicket).isNotNull();
        assertThat(actualCheckInTicket.vehicleDto()).isEqualTo(vehicle);
        assertThat(actualCheckInTicket.entryTime()).isNotNull();
        assertThat(actualCheckInTicket.parkingSlotDto()).isNotNull();

        assertThat(actualParkingSlot.getStatus()).isEqualTo(SlotStatus.OCCUPIED);
        assertThat(actualParkingSlot.getType()).isIn(SlotCompatibilityConvertorUtils.getCompatibleTypes(vehicle.vehicleType()));
    }

    static Stream<Arguments> provideCheckInParameters() {
        return Stream.of(
                Arguments.of(new VehicleDto("AA8672BB", VehicleType.MOTORCYCLE)),
                Arguments.of(new VehicleDto("RYA6741OP", VehicleType.CAR)),
                Arguments.of(new VehicleDto("AY2134LN", VehicleType.TRUCK)));
    }

    @AfterAll
    static void tearDown() {
        TestContainersConfig.stopTestContainers();
    }
}
