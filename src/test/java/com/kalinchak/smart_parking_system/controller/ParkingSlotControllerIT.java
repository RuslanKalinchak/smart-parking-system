package com.kalinchak.smart_parking_system.controller;


import com.kalinchak.smart_parking_system.controller.configuration.TestContainersConfig;
import com.kalinchak.smart_parking_system.factory.ParkingSlotDtoFactory;
import com.kalinchak.smart_parking_system.model.dto.ParkingSlotDto;
import com.kalinchak.smart_parking_system.repository.ParkingSlotRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = TestContainersConfig.class)
@ActiveProfiles("integration-test")
public class ParkingSlotControllerIT {

    private RestClient restClient;
    @LocalServerPort
    private int port;

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder().baseUrl("http://localhost:" + port).build();
    }

    @Test
    void givenValidParkingSlot_whenAddParkingSlot_thenReturnSuccessResponse() {

        //Given
        long lotId = 1L;
        ParkingSlotDto parkingSlotDto = ParkingSlotDtoFactory.getParkingSlotInstanceCompact(1);
        URI uri = UriComponentsBuilder.fromPath("/api/admin/slots/add-slot")
                .queryParam("lotId", lotId)
                .build().toUri();

        //When
        ResponseEntity<ParkingSlotDto> response = restClient.post()
                .uri(uri)
                .body(parkingSlotDto)
                .retrieve()
                .toEntity(ParkingSlotDto.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ParkingSlotDto actualParkingSlotDto = response.getBody();
        assertThat(actualParkingSlotDto).isNotNull();
        assertThat(actualParkingSlotDto.id()).isNotNull();
        assertThat(actualParkingSlotDto.slotCode()).isEqualTo(parkingSlotDto.slotCode());
        assertThat(actualParkingSlotDto.levelNumber()).isEqualTo(parkingSlotDto.levelNumber());
    }

    @Test
    void givenAddedParkingSlot_whenRemoveParkingSlot_thenReturnSuccessResponse() {

        //Given
        long lotId = 1L;
        ParkingSlotDto parkingSlotDto = ParkingSlotDtoFactory.getParkingSlotInstanceMotorcycle(1);
        URI addLotUri = UriComponentsBuilder.fromPath("/api/admin/slots/add-slot")
                .queryParam("lotId", lotId)
                .build().toUri();

        ResponseEntity<ParkingSlotDto> addSlotResponse = restClient.post()
                .uri(addLotUri)
                .body(parkingSlotDto)
                .retrieve()
                .toEntity(ParkingSlotDto.class);

        long addedParkingSlotId = addSlotResponse.getBody().id();

        //When
        URI removeLotUri = UriComponentsBuilder.fromPath("/api/admin/slots/remove-slot/{slotId}")
                .buildAndExpand(addedParkingSlotId)
                .toUri();

        ResponseEntity<Void> removeSlotResponse = restClient.delete()
                .uri(removeLotUri)
                .retrieve().toBodilessEntity();

        //Then
        assertThat(removeSlotResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(parkingSlotRepository.existsById(addedParkingSlotId)).isFalse();
    }

    @AfterAll
    static void tearDown() {
        TestContainersConfig.stopTestContainers();
    }
}
