package com.example.roenbeauty.reservation.controller;

import com.example.roenbeauty.reservation.dto.ReservationCreateRequestDto;
import com.example.roenbeauty.reservation.dto.ReservationResponseDto;
import com.example.roenbeauty.reservation.dto.ReservationUpdateStatusRequestDto;
import com.example.roenbeauty.reservation.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ReservationResponseDto createReservation(@RequestBody ReservationCreateRequestDto requestDto) {
        return reservationService.createReservation(requestDto);
    }

    @GetMapping
    public List<ReservationResponseDto> getReservations() {
        return reservationService.getReservations();
    }

    @PatchMapping("/{id}/status")
    public ReservationResponseDto updateReservationStatus(
            @PathVariable("id") Long id,
            @RequestBody ReservationUpdateStatusRequestDto requestDto
    ) {
        return reservationService.updateReservationStatus(id, requestDto);
    }
}