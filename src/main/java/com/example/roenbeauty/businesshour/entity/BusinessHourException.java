package com.example.roenbeauty.businesshour.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "business_hour_exceptions")
public class BusinessHourException {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate date;

    private LocalTime openTime;

    private LocalTime closeTime;

    @Column(nullable = false)
    private Boolean closed;

    protected BusinessHourException() {
    }

    public BusinessHourException(LocalDate date, LocalTime openTime, LocalTime closeTime, Boolean closed) {
        this.date = date;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.closed = closed;
    }

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

    public void update(LocalTime openTime, LocalTime closeTime, Boolean closed) {
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.closed = closed;
    }
}