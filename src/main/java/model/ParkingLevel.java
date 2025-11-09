package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "parking_levels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLevel {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "level_number", nullable = false)
    private int levelNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_lot_id", nullable = false)
    private ParkingLot parkingLot;

    @OneToMany(mappedBy = "parkingLevel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParkingSlot> slots;
}
