package com.example.roenbeauty.reservation.service;

import com.example.roenbeauty.reservation.dto.ReservationCreateRequestDto;
import com.example.roenbeauty.reservation.dto.ReservationResponseDto;
import com.example.roenbeauty.reservation.entity.Reservation;
import com.example.roenbeauty.reservation.enums.ReservationStatus;
import com.example.roenbeauty.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public ReservationResponseDto createReservation(ReservationCreateRequestDto requestDto) {
        Reservation reservation = new Reservation(
                requestDto.getName(),
                requestDto.getPhone(),
                requestDto.getReservationDate(),
                requestDto.getReservationTime(),
                requestDto.getServiceName(),
                requestDto.getRequestMemo(),
                ReservationStatus.PENDING
        );

        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponseDto.from(savedReservation);
    }
}