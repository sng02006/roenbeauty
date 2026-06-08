package com.example.roenbeauty.reservation.dto;

import com.example.roenbeauty.reservation.entity.Reservation;
import com.example.roenbeauty.reservation.enums.CanceledBy;
import com.example.roenbeauty.reservation.enums.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservationResponseDto {

    private Long id;
    private String name;
    private String phone;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private String serviceName;
    private String requestMemo;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer durationMinutes;

    private CanceledBy canceledBy;
    private String cancelReason;
    private LocalDateTime canceledAt;

    public ReservationResponseDto(
            Long id,
            String name,
            String phone,
            LocalDate reservationDate,
            LocalTime reservationTime,
            String serviceName,
            String requestMemo,
            ReservationStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Integer durationMinutes,
            CanceledBy canceledBy,
            String cancelReason,
            LocalDateTime canceledAt
    ) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.serviceName = serviceName;
        this.requestMemo = requestMemo;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.durationMinutes = durationMinutes;
        this.canceledBy = canceledBy;
        this.cancelReason = cancelReason;
        this.canceledAt = canceledAt;
    }

    public static ReservationResponseDto from(Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getName(),
                reservation.getPhone(),
                reservation.getReservationDate(),
                reservation.getReservationTime(),
                reservation.getServiceName(),
                reservation.getRequestMemo(),
                reservation.getStatus(),
                reservation.getCreatedAt(),
                reservation.getUpdatedAt(),
                reservation.getDurationMinutes(),
                reservation.getCanceledBy(),
                reservation.getCancelReason(),
                reservation.getCanceledAt()
        );
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
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
}