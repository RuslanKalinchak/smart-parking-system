package com.kalinchak.smart_parking_system.repository;

import com.kalinchak.smart_parking_system.model.CheckInStatus;
import com.kalinchak.smart_parking_system.model.CheckInTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CheckInTicketRepository extends JpaRepository<CheckInTicket, Long> {

    Optional<CheckInTicket> findByVehicle_LicensePlateAndStatus(
            String licensePlate,
            CheckInStatus status
    );
}
