package com.example.roenbeauty.reservation.entity;

import com.example.roenbeauty.reservation.enums.ReservationStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false)
    private LocalDate reservationDate;

    @Column(nullable = false)
    private LocalTime reservationTime;

    @Column(nullable = false, length = 100)
    private String serviceName;

    @Column(length = 500)
    private String requestMemo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    protected Reservation() {
    }

    public Reservation(
            String name,
            String phone,
            LocalDate reservationDate,
            LocalTime reservationTime,
            String serviceName,
            String requestMemo,
            ReservationStatus status
    ) {
        this.name = name;
        this.phone = phone;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.serviceName = serviceName;
        this.requestMemo = requestMemo;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public LocalTime getReservationTime() {
        return reservationTime;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getRequestMemo() {
        return requestMemo;
    }

    public ReservationStatus getStatus() {
        return status;
    }
}