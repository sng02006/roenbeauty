package com.example.roenbeauty.businesshour.entity;

import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "business_hours")
public class BusinessHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private DayOfWeek dayOfWeek;

    private LocalTime openTime;

    private LocalTime closeTime;

    @Column(nullable = false)
    private Boolean closed;

    protected BusinessHour() {
    }

    public BusinessHour(DayOfWeek dayOfWeek, LocalTime openTime, LocalTime closeTime, Boolean closed) {
        this.dayOfWeek = dayOfWeek;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.closed = closed;
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

    public void update(LocalTime openTime, LocalTime closeTime, Boolean closed) {
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.closed = closed;
    }
}