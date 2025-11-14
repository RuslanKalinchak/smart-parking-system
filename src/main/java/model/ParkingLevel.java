package model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
