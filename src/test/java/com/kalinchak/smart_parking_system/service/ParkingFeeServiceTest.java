package com.kalinchak.smart_parking_system.service;

import com.kalinchak.smart_parking_system.model.VehicleType;
import com.kalinchak.smart_parking_system.service.impl.ParkingFeeServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ParkingFeeServiceTest {

    private final ParkingFeeService parkingFeeService = new ParkingFeeServiceImpl();

    @ParameterizedTest(name = "{index} => type={0}, minutes={1}, expected={2}")
    @MethodSource("feeTestData")
    @DisplayName("Should correctly calculate parking fees for all strategies")
    void testCalculateFee(VehicleType type, long minutes, BigDecimal expectedFee) {

        BigDecimal fee = parkingFeeService.calculateFee(type, minutes);

        assertThat(fee)
                .as("Fee for %s for %d minutes", type, minutes)
                .isEqualByComparingTo(expectedFee);
    }

    static Stream<Object[]> feeTestData() {
        return Stream.of(
                new Object[]{VehicleType.MOTORCYCLE, 60L, BigDecimal.valueOf(1)},
                new Object[]{VehicleType.MOTORCYCLE, 90L, BigDecimal.valueOf(1.5)},
                new Object[]{VehicleType.CAR, 60L, BigDecimal.valueOf(2)},
                new Object[]{VehicleType.CAR, 30L, BigDecimal.valueOf(1)},
                new Object[]{VehicleType.TRUCK, 120L, BigDecimal.valueOf(6)},
                new Object[]{VehicleType.TRUCK, 45L, BigDecimal.valueOf(2.25)}
        );
    }
}
