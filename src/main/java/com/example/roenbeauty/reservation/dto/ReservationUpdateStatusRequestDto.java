package com.example.roenbeauty.reservation.dto;

import com.example.roenbeauty.reservation.enums.ReservationStatus;

public class ReservationUpdateStatusRequestDto {

    private ReservationStatus status;

    public ReservationUpdateStatusRequestDto() {
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}