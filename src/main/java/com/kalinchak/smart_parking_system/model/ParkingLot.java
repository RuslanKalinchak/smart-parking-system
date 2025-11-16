package com.kalinchak.smart_parking_system.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "parking_lots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingLot {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParkingLevel> parkingLevels;
}
