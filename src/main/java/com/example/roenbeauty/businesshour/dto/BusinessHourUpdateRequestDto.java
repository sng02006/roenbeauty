package com.example.roenbeauty.businesshour.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class BusinessHourUpdateRequestDto {

    private DayOfWeek dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Boolean closed;

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
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