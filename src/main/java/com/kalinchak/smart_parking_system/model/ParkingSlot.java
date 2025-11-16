package com.kalinchak.smart_parking_system.model;

import com.kalinchak.smart_parking_system.model.dto.ParkingSlotDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

@Entity
@Table(name = "parking_slots", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"level_id", "slot_code"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSlot {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "slot_code", nullable = false)
    private String slotCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id")
    private ParkingLevel parkingLevel;
}
