package com.example.roenbeauty.reservation.service;

import com.example.roenbeauty.reservation.dto.ReservationCreateRequestDto;
import com.example.roenbeauty.reservation.dto.ReservationResponseDto;
import com.example.roenbeauty.reservation.dto.ReservationUpdateStatusRequestDto;
import com.example.roenbeauty.reservation.entity.Reservation;
import com.example.roenbeauty.reservation.enums.ReservationStatus;
import com.example.roenbeauty.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public ReservationResponseDto createReservation(ReservationCreateRequestDto requestDto) {
        validateDuplicatedReservationTime(
                requestDto.getReservationDate(),
                requestDto.getReservationTime()
        );

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

    private void validateDuplicatedReservationTime(
            LocalDate reservationDate,
            LocalTime reservationTime
    ) {
        boolean exists = reservationRepository.existsByReservationDateAndReservationTimeAndStatusNot(
                reservationDate,
                reservationTime,
                ReservationStatus.CANCELED
        );

        if (exists) {
            throw new IllegalArgumentException("이미 예약된 시간입니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<String> getReservedTimes(LocalDate reservationDate) {
        return reservationRepository
                .findByReservationDateAndStatusNotOrderByReservationTimeAsc(
                        reservationDate,
                        ReservationStatus.CANCELED
                )
                .stream()
                .map(reservation -> reservation.getReservationTime().toString())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDto> getReservations(
            ReservationStatus status,
            LocalDate reservationDate
    ) {
        List<Reservation> reservations;

        if (status != null && reservationDate != null) {
            reservations = reservationRepository
                    .findByStatusAndReservationDateOrderByReservationTimeAsc(status, reservationDate);
        } else if (status != null) {
            reservations = reservationRepository
                    .findByStatusOrderByReservationDateAscReservationTimeAsc(status);
        } else if (reservationDate != null) {
            reservations = reservationRepository
                    .findByReservationDateOrderByReservationTimeAsc(reservationDate);
        } else {
            reservations = reservationRepository
                    .findAllByOrderByReservationDateAscReservationTimeAsc();
        }

        return reservations.stream()
                .map(ReservationResponseDto::from)
                .toList();
    }

    @Transactional
    public ReservationResponseDto updateReservationStatus(
            Long id,
            ReservationUpdateStatusRequestDto requestDto
    ) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        reservation.updateStatus(requestDto.getStatus());

        return ReservationResponseDto.from(reservation);
    }
}