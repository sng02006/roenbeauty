package com.example.roenbeauty.reservation.service;

import com.example.roenbeauty.menu.entity.Menu;
import com.example.roenbeauty.menu.repository.MenuRepository;
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
    private final MenuRepository menuRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            MenuRepository menuRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.menuRepository = menuRepository;
    }

    public ReservationResponseDto createReservation(ReservationCreateRequestDto requestDto) {
        Menu menu = menuRepository.findById(requestDto.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

        validateReservationTime(
                requestDto.getReservationDate(),
                requestDto.getReservationTime(),
                menu.getDurationMinutes()
        );

        Reservation reservation = new Reservation(
                requestDto.getName(),
                requestDto.getPhone(),
                requestDto.getReservationDate(),
                requestDto.getReservationTime(),
                menu.getName(),
                requestDto.getRequestMemo(),
                ReservationStatus.PENDING,
                menu.getDurationMinutes()
        );

        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponseDto.from(savedReservation);
    }

    private void validateReservationTime(
            LocalDate reservationDate,
            LocalTime newStartTime,
            Integer newDurationMinutes
    ) {
        LocalTime newEndTime = newStartTime.plusMinutes(newDurationMinutes);

        List<Reservation> reservations = reservationRepository
                .findByReservationDateAndStatusNot(
                        reservationDate,
                        ReservationStatus.CANCELED
                );

        for (Reservation reservation : reservations) {
            LocalTime existingStartTime = reservation.getReservationTime();
            LocalTime existingEndTime = existingStartTime.plusMinutes(reservation.getDurationMinutes());

            boolean isOverlapped =
                    newStartTime.isBefore(existingEndTime)
                            && newEndTime.isAfter(existingStartTime);

            if (isOverlapped) {
                throw new IllegalArgumentException("이미 예약된 시간과 겹칩니다. 다른 시간을 선택해주세요.");
            }
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
    public ReservationResponseDto updateReservationStatus(Long id, ReservationUpdateStatusRequestDto requestDto) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약이 존재하지 않습니다."));

        validateStatusChange(reservation.getStatus(), requestDto.getStatus());

        reservation.updateStatus(requestDto.getStatus());

        return ReservationResponseDto.from(reservation);
    }

    private void validateStatusChange(ReservationStatus current, ReservationStatus target) {
        if (current == ReservationStatus.CANCELED || current == ReservationStatus.COMPLETED) {
            throw new IllegalArgumentException("이미 종료된 예약은 상태 변경이 불가능합니다.");
        }

        if (current == ReservationStatus.PENDING) {
            if (target != ReservationStatus.CONFIRMED && target != ReservationStatus.CANCELED) {
                throw new IllegalArgumentException("대기 상태에서는 확정 또는 취소만 가능합니다.");
            }
        }

        if (current == ReservationStatus.CONFIRMED) {
            if (target != ReservationStatus.COMPLETED && target != ReservationStatus.CANCELED) {
                throw new IllegalArgumentException("확정 상태에서는 완료 또는 취소만 가능합니다.");
            }
        }
    }
}