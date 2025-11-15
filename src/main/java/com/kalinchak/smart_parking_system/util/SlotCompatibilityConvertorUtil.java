package com.kalinchak.smart_parking_system.util;

import com.kalinchak.smart_parking_system.model.SlotType;
import com.kalinchak.smart_parking_system.model.VehicleType;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class SlotCompatibilityConvertorUtil {

    public static List<SlotType> getCompatibleTypes(VehicleType vehicleType) {
        return switch (vehicleType) {
            case MOTORCYCLE -> List.of(SlotType.MOTORCYCLE, SlotType.COMPACT, SlotType.LARGE);
            case CAR        -> List.of(SlotType.COMPACT, SlotType.LARGE);
            case TRUCK      -> List.of(SlotType.LARGE);
        };
    }
}
