package com.example.roenbeauty.reservation.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationCreateRequestDto {

    private String name;
    private String phone;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private String serviceName;
    private String requestMemo;

    public ReservationCreateRequestDto() {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public void setReservationTime(LocalTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setRequestMemo(String requestMemo) {
        this.requestMemo = requestMemo;
    }
}