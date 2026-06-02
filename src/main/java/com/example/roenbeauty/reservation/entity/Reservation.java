package com.example.roenbeauty.reservation.entity;

import com.example.roenbeauty.global.common.BaseTimeEntity;
import com.example.roenbeauty.reservation.enums.CanceledBy;
import com.example.roenbeauty.reservation.enums.ReservationStatus;
import com.example.roenbeauty.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "reservations")
public class Reservation extends BaseTimeEntity {

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
    @Column(nullable = false, length = 30)
    private ReservationStatus status;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private CanceledBy canceledBy;

    @Column(length = 500)
    private String cancelReason;

    private LocalDateTime canceledAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    protected Reservation() {
    }

    public Reservation(
            String name,
            String phone,
            LocalDate reservationDate,
            LocalTime reservationTime,
            String serviceName,
            String requestMemo,
            ReservationStatus status,
            Integer durationMinutes,
            User user
    ) {
        this.name = name;
        this.phone = phone;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.serviceName = serviceName;
        this.requestMemo = requestMemo;
        this.status = status;
        this.durationMinutes = durationMinutes;
        this.user = user;
    }

    public void waitingPayment() {
        this.status = ReservationStatus.WAITING_PAYMENT;
    }

    public void paid() {
        this.status = ReservationStatus.PAID;
    }

    public void confirm() {
        this.status = ReservationStatus.CONFIRMED;
    }

    public void complete() {
        this.status = ReservationStatus.COMPLETED;
    }

    public void cancel(CanceledBy canceledBy, String cancelReason) {
        this.status = ReservationStatus.CANCELED;
        this.canceledBy = canceledBy;
        this.cancelReason = cancelReason;
        this.canceledAt = LocalDateTime.now();
    }

    public void updateStatus(ReservationStatus status) {
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

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public CanceledBy getCanceledBy() {
        return canceledBy;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public LocalDateTime getCanceledAt() {
        return canceledAt;
    }

    public User getUser() {
        return user;
    }
}