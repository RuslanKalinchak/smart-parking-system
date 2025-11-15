package com.kalinchak.smart_parking_system.model;

import lombok.Getter;

@Getter
public enum SlotType {
    MOTORCYCLE(1),
    COMPACT(2),
    LARGE(3),
    HANDICAPPED(0);

    private final int sizePriority;

    SlotType(int sizePriority) {
        this.sizePriority = sizePriority;
    }
}
