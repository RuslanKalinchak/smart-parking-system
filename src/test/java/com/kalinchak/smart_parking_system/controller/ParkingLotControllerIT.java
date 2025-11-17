package com.kalinchak.smart_parking_system.controller;

import com.kalinchak.smart_parking_system.controller.configuration.TestContainersConfig;
import com.kalinchak.smart_parking_system.factory.ParkingLotDtoFactory;
import com.kalinchak.smart_parking_system.model.dto.ParkingLotDto;
import com.kalinchak.smart_parking_system.repository.ParkingLotRepository;
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
class ParkingLotControllerIT {

    private RestClient restClient;
    @LocalServerPort
    private int port;

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder().baseUrl("http://localhost:" + port).build();
    }

    @Test
    void givenValidParkingLot_whenAddParkingLot_thenReturnSuccessResponse() {

        //Given
        ParkingLotDto parkingLotDto = ParkingLotDtoFactory.getParkingLotInstance(4);
        URI uri = UriComponentsBuilder.fromPath("/api/admin/lots/add-lot").build().toUri();

        //When
        ResponseEntity<ParkingLotDto> response = restClient.post()
                .uri(uri)
                .body(parkingLotDto)
                .retrieve()
                .toEntity(ParkingLotDto.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ParkingLotDto actualParkingLotDto = response.getBody();
        assertThat(actualParkingLotDto).isNotNull();
        assertThat(actualParkingLotDto.id()).isNotNull();
    }

    @Test
    void givenAddedParkingLot_whenRemoveParkingLot_thenReturnSuccessResponse() {

        //Given
        ParkingLotDto parkingLotDto = ParkingLotDtoFactory.getParkingLotInstance(5);
        URI addLotUri = UriComponentsBuilder.fromPath("/api/admin/lots/add-lot").build().toUri();

        ResponseEntity<ParkingLotDto> addLotResponse = restClient.post()
                .uri(addLotUri)
                .body(parkingLotDto)
                .retrieve()
                .toEntity(ParkingLotDto.class);

        long addedParkingLotId = addLotResponse.getBody().id();

        //When
        URI removeLotUri = UriComponentsBuilder.fromPath("/api/admin/lots/remove-lot/{lotId}")
                .buildAndExpand(addedParkingLotId)
                .toUri();

        ResponseEntity<Void> removeLotResponse = restClient.delete()
                .uri(removeLotUri)
                .retrieve().toBodilessEntity();

        //Then
        assertThat(removeLotResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(parkingLotRepository.existsById(addedParkingLotId)).isFalse();
    }

    @AfterAll
    static void tearDown() {
        TestContainersConfig.stopTestContainers();
    }
}
