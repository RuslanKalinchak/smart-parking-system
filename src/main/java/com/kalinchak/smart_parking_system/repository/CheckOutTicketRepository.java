package com.kalinchak.smart_parking_system.repository;

import com.kalinchak.smart_parking_system.model.entity.CheckOutTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckOutTicketRepository extends JpaRepository<CheckOutTicket, Long> {
}
