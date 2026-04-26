package com.example.roenbeauty.blockedtime.dto;

import com.example.roenbeauty.blockedtime.entity.BlockedTime;

import java.time.LocalDate;
import java.time.LocalTime;

public class BlockedTimeResponseDto {

    private Long id;
    private LocalDate blockedDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String reason;

    public BlockedTimeResponseDto(Long id, LocalDate blockedDate, LocalTime startTime, LocalTime endTime, String reason) {
        this.id = id;
        this.blockedDate = blockedDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
    }

    public static BlockedTimeResponseDto from(BlockedTime blockedTime) {
        return new BlockedTimeResponseDto(
                blockedTime.getId(),
                blockedTime.getBlockedDate(),
                blockedTime.getStartTime(),
                blockedTime.getEndTime(),
                blockedTime.getReason()
        );
    }

    public Long getId() {
        return id;
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
}