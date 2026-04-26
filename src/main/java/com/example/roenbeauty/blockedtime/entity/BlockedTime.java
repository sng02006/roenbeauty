package com.example.roenbeauty.blockedtime.entity;

import com.example.roenbeauty.global.common.BaseTimeEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "blocked_times")
public class BlockedTime extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate blockedDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(length = 500)
    private String reason;

    protected BlockedTime() {
    }

    public BlockedTime(LocalDate blockedDate, LocalTime startTime, LocalTime endTime, String reason) {
        this.blockedDate = blockedDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
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