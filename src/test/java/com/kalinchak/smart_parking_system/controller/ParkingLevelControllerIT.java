package com.kalinchak.smart_parking_system.controller;

import com.kalinchak.smart_parking_system.controller.configuration.TestContainersConfig;
import com.kalinchak.smart_parking_system.factory.ParkingLevelDtoFactory;
import com.kalinchak.smart_parking_system.model.dto.ParkingLevelDto;
import com.kalinchak.smart_parking_system.repository.ParkingLevelRepository;
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
public class ParkingLevelControllerIT {

    private RestClient restClient;
    @LocalServerPort
    private int port;

    @Autowired
    private ParkingLevelRepository parkingLevelRepository;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder().baseUrl("http://localhost:" + port).build();
    }

    @Test
    void givenValidParkingLevel_whenAddParkingLevel_thenReturnSuccessResponse() {

        //Given
        long lotId = 1L;
        ParkingLevelDto parkingLevelDto = ParkingLevelDtoFactory.getParkingLevelInstance(4);
        URI uri = UriComponentsBuilder.fromPath("/api/admin/levels/add-level")
                .queryParam("lotId", lotId)
                .build().toUri();

        //When
        ResponseEntity<ParkingLevelDto> response = restClient.post()
                .uri(uri)
                .body(parkingLevelDto)
                .retrieve()
                .toEntity(ParkingLevelDto.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ParkingLevelDto actualParkingLevelDto = response.getBody();
        assertThat(actualParkingLevelDto).isNotNull();
        assertThat(actualParkingLevelDto.id()).isNotNull();
        assertThat(actualParkingLevelDto.levelNumber()).isEqualTo(parkingLevelDto.levelNumber());
        assertThat(actualParkingLevelDto.slots()).isNotNull();
    }

    @Test
    void givenAddedParkingLevel_whenRemoveParkingLevel_thenReturnSuccessResponse() {

        //Given
        long lotId = 1L;
        ParkingLevelDto parkingLevelDto = ParkingLevelDtoFactory.getParkingLevelInstance(5);
        URI addLotUri = UriComponentsBuilder.fromPath("/api/admin/levels/add-level")
                .queryParam("lotId", lotId)
                .build().toUri();

        ResponseEntity<ParkingLevelDto> addLevelResponse = restClient.post()
                .uri(addLotUri)
                .body(parkingLevelDto)
                .retrieve()
                .toEntity(ParkingLevelDto.class);

        long addedParkingLevelId = addLevelResponse.getBody().id();

        //When
        URI removeLotUri = UriComponentsBuilder.fromPath("/api/admin/levels/remove-level/{levelId}")
                .buildAndExpand(addedParkingLevelId)
                .toUri();

        ResponseEntity<Void> removeLotResponse = restClient.delete()
                .uri(removeLotUri)
                .retrieve().toBodilessEntity();

        //Then
        assertThat(removeLotResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(parkingLevelRepository.existsById(addedParkingLevelId)).isFalse();
    }

    @AfterAll
    static void tearDown() {
        TestContainersConfig.stopTestContainers();
    }
}
