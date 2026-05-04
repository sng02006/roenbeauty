package com.example.roenbeauty.businesshour.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class BusinessHourExceptionRequestDto {

    private LocalDate date;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Boolean closed;

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }

    public Boolean getClosed() {
        return closed;
    }
}