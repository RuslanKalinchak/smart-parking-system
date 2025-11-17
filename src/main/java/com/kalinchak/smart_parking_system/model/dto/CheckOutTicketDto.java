package com.kalinchak.smart_parking_system.model.dto;

import com.kalinchak.smart_parking_system.model.entity.CheckOutTicket;
import com.kalinchak.smart_parking_system.util.TimeFormatUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CheckOutTicketDto(LocalDateTime entryTime, LocalDateTime exitTime, String totalDuration, BigDecimal fee) {

    public CheckOutTicketDto(final CheckOutTicket checkOutTicket) {
        this(checkOutTicket.getCheckInTicket().getEntryTime(),
                checkOutTicket.getExitTime(),
                TimeFormatUtils.formatDuration(checkOutTicket.getCheckInTicket().getEntryTime(), checkOutTicket.getExitTime()),
                checkOutTicket.getFee());
    }
}
