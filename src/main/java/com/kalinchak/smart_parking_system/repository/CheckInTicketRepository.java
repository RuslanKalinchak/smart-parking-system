package com.kalinchak.smart_parking_system.repository;

import com.kalinchak.smart_parking_system.model.CheckInStatus;
import com.kalinchak.smart_parking_system.model.entity.CheckInTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheckInTicketRepository extends JpaRepository<CheckInTicket, Long> {

    Optional<CheckInTicket> findByVehicle_LicensePlateAndStatus(
            final String licensePlate,
            final CheckInStatus status
    );

    List<CheckInTicket> findAllByStatus(final CheckInStatus status);
}
