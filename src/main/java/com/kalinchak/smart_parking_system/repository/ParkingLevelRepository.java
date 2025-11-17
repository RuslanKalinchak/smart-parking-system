package com.kalinchak.smart_parking_system.repository;

import com.kalinchak.smart_parking_system.model.entity.ParkingLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkingLevelRepository extends JpaRepository<ParkingLevel, Long> {

    Optional<ParkingLevel> findByLevelNumberAndParkingLotId(final int levelNumber, final long parkingLotId);
}
