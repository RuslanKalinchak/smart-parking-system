package com.kalinchak.smart_parking_system.repository;

import com.kalinchak.smart_parking_system.model.entity.ParkingSlot;
import com.kalinchak.smart_parking_system.model.SlotStatus;
import com.kalinchak.smart_parking_system.model.SlotType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    List<ParkingSlot> findByTypeInAndStatus(final List<SlotType> types, final SlotStatus status);

    Optional<ParkingSlot> findBySlotCode(final String slotCode);
}
