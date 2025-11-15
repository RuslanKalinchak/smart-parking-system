package com.kalinchak.smart_parking_system.repository;

import com.kalinchak.smart_parking_system.model.ParkingSlot;
import com.kalinchak.smart_parking_system.model.SlotStatus;
import com.kalinchak.smart_parking_system.model.SlotType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    List<ParkingSlot> findByTypeInAndStatus(List<SlotType> types, SlotStatus status);
}
