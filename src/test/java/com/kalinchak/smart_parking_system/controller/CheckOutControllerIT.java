package com.kalinchak.smart_parking_system.controller;

import com.kalinchak.smart_parking_system.controller.configuration.TestContainersConfig;
import com.kalinchak.smart_parking_system.model.*;
import com.kalinchak.smart_parking_system.model.dto.CheckOutTicketDto;
import com.kalinchak.smart_parking_system.model.entity.CheckInTicket;
import com.kalinchak.smart_parking_system.model.entity.CheckOutTicket;
import com.kalinchak.smart_parking_system.model.entity.ParkingSlot;
import com.kalinchak.smart_parking_system.repository.CheckInTicketRepository;
import com.kalinchak.smart_parking_system.repository.CheckOutTicketRepository;
import com.kalinchak.smart_parking_system.repository.ParkingSlotRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Assertions.within;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = TestContainersConfig.class)
@ActiveProfiles("integration-test")
class CheckOutControllerIT {

    private RestClient restClient;
    @LocalServerPort
    private int port;
    @Autowired
    private CheckInTicketRepository checkInTicketRepository;
    @Autowired
    private CheckOutTicketRepository checkOutTicketRepository;
    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder().baseUrl("http://localhost:" + port).build();
    }

    @ParameterizedTest
    @CsvSource({
            "AA1234BB, 1",
            "CE5482EH, 2",
            "AB9021KM, 3"
    })
    @Sql(scripts = "/sql/check_out_clean_script.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void givenValidCheckIn_whenVehicleCheckOut_thenSuccessReturnCheckOutTicket(final String licensePlate, final long checkInTicketId) {
        //Given
        URI uri = UriComponentsBuilder.fromPath("/api/check-out/{licensePlate}")
                .buildAndExpand(licensePlate).toUri();

        String expectedDurationPattern = "^(\\d+ day[s]? )?(\\d+ hour[s]? )?(\\d+ minute[s]? )?(\\d+ second[s]?)$";

        //When
        ResponseEntity<CheckOutTicketDto> response = restClient.post()
                .uri(uri)
                .retrieve()
                .toEntity(CheckOutTicketDto.class);

        //Then
        CheckOutTicketDto actualBody = response.getBody();
        CheckInTicket actualCheckInTicket = checkInTicketRepository.findById(checkInTicketId).orElseThrow();
        CheckOutTicket actualCheckOutTicket = checkOutTicketRepository.findById(checkInTicketId).orElseThrow();
        ParkingSlot actualParkingSlot = parkingSlotRepository.findById(actualCheckInTicket.getParkingSlot().getId()).orElseThrow();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(actualBody).isNotNull();
        assertThat(actualBody.entryTime())
                .isCloseTo(actualCheckInTicket.getEntryTime(), within(1, ChronoUnit.MILLIS));
        assertThat(actualBody.exitTime())
                .isCloseTo(actualCheckOutTicket.getExitTime(), within(1, ChronoUnit.MILLIS));

        assertThat(actualBody.fee()).isPositive();
        assertThat(actualBody.totalDuration()).matches(expectedDurationPattern);

        assertThat(actualCheckInTicket.getStatus()).isEqualTo(CheckInStatus.INACTIVE);
        assertThat(actualParkingSlot.getStatus()).isEqualTo(SlotStatus.AVAILABLE);
    }

    @AfterAll
    static void tearDown() {
        TestContainersConfig.stopTestContainers();
    }
}
