package com.example.roenbeauty.reservation.controller;

import com.example.roenbeauty.global.dto.AuthUser;
import com.example.roenbeauty.payment.dto.CheckoutResponseDto;
import com.example.roenbeauty.reservation.dto.ReservationCreateRequestDto;
import com.example.roenbeauty.reservation.dto.ReservationResponseDto;
import com.example.roenbeauty.reservation.dto.ReservationUpdateStatusRequestDto;
import com.example.roenbeauty.reservation.enums.ReservationStatus;
import com.example.roenbeauty.reservation.service.ReservationService;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public CheckoutResponseDto createReservation(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody ReservationCreateRequestDto requestDto
    ) {
        return reservationService.createReservation(authUser, requestDto);
    }

    @GetMapping
    public Page<ReservationResponseDto> getReservations(
            @RequestParam(name = "status", required = false) ReservationStatus status,
            @RequestParam(name = "reservationDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate reservationDate,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return reservationService.getReservations(status, reservationDate, keyword, page, size);
    }

    @PatchMapping("/{id}/status")
    public ReservationResponseDto updateReservationStatus(
            @PathVariable("id") Long id,
            @RequestBody ReservationUpdateStatusRequestDto requestDto
    ) {
        return reservationService.updateReservationStatus(id, requestDto);
    }

    @GetMapping("/reserved-times")
    public List<String> getReservedTimes(
            @RequestParam(name = "reservationDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate reservationDate
    ) {
        return reservationService.getReservedTimes(reservationDate);
    }

    @GetMapping("/available-times")
    public List<String> getAvailableTimes(
            @RequestParam(name = "reservationDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate reservationDate,
            @RequestParam(name = "menuId") Long menuId
    ) {
        return reservationService.getAvailableTimes(reservationDate, menuId);
    }

    @GetMapping("/my")
    public Page<ReservationResponseDto> getMyReservations(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {
        return reservationService.getMyReservations(
                authUser,
                page,
                size
        );
    }
}