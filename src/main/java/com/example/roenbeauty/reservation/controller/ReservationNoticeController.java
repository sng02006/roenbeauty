package com.example.roenbeauty.reservation.controller;

import com.example.roenbeauty.reservation.dto.ReservationNoticeResponseDto;
import com.example.roenbeauty.reservation.dto.ReservationNoticeUpdateRequestDto;
import com.example.roenbeauty.reservation.service.ReservationNoticeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservation-notice")
public class ReservationNoticeController {

    private final ReservationNoticeService reservationNoticeService;

    public ReservationNoticeController(ReservationNoticeService reservationNoticeService) {
        this.reservationNoticeService = reservationNoticeService;
    }

    @GetMapping
    public ReservationNoticeResponseDto getNotice() {
        return reservationNoticeService.getNotice();
    }

    @PutMapping
    public ReservationNoticeResponseDto updateNotice(
            @RequestBody ReservationNoticeUpdateRequestDto requestDto
    ) {
        return reservationNoticeService.updateNotice(requestDto);
    }
}