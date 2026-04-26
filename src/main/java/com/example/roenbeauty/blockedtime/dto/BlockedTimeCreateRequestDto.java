package com.example.roenbeauty.blockedtime.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class BlockedTimeCreateRequestDto {

    private LocalDate blockedDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String reason;

    public BlockedTimeCreateRequestDto() {
    }

    public LocalDate getBlockedDate() {
        return blockedDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getReason() {
        return reason;
    }

    public void setBlockedDate(LocalDate blockedDate) {
        this.blockedDate = blockedDate;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}