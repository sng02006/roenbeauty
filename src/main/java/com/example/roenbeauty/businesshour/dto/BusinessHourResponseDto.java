package com.example.roenbeauty.businesshour.dto;

import com.example.roenbeauty.businesshour.entity.BusinessHour;
import com.example.roenbeauty.businesshour.entity.BusinessHourException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class BusinessHourResponseDto {

    private LocalDate date;
    private DayOfWeek dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Boolean closed;

    public BusinessHourResponseDto(LocalDate date, DayOfWeek dayOfWeek, LocalTime openTime, LocalTime closeTime, Boolean closed) {
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.closed = closed;
    }

    public static BusinessHourResponseDto from(BusinessHour businessHour) {
        return new BusinessHourResponseDto(
                null,
                businessHour.getDayOfWeek(),
                businessHour.getOpenTime(),
                businessHour.getCloseTime(),
                businessHour.getClosed()
        );
    }

    public static BusinessHourResponseDto from(BusinessHourException exception) {
        return new BusinessHourResponseDto(
                exception.getDate(),
                null,
                exception.getOpenTime(),
                exception.getCloseTime(),
                exception.getClosed()
        );
    }

    public LocalDate getDate() {
        return date;
    }

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