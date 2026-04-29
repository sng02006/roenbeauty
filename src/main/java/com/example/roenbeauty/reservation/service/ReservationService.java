package com.example.roenbeauty.reservation.service;

import com.example.roenbeauty.blockedtime.entity.BlockedTime;
import com.example.roenbeauty.blockedtime.repository.BlockedTimeRepository;
import com.example.roenbeauty.businesshour.entity.BusinessHour;
import com.example.roenbeauty.businesshour.repository.BusinessHourRepository;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MenuRepository menuRepository;
    private final BusinessHourRepository businessHourRepository;
    private final BlockedTimeRepository blockedTimeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            MenuRepository menuRepository,
            BusinessHourRepository businessHourRepository,
            BlockedTimeRepository blockedTimeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.menuRepository = menuRepository;
        this.businessHourRepository = businessHourRepository;
        this.blockedTimeRepository = blockedTimeRepository;
    }

    public ReservationResponseDto createReservation(ReservationCreateRequestDto requestDto) {
        Menu menu = menuRepository.findById(requestDto.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

        validateReservationDate(requestDto.getReservationDate());

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

    private void validateReservationDate(LocalDate reservationDate) {
        if (reservationDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("지난 날짜는 예약할 수 없습니다.");
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

    @Transactional(readOnly = true)
    public List<String> getAvailableTimes(LocalDate reservationDate, Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

        List<Reservation> reservations = reservationRepository
                .findByReservationDateAndStatusNot(
                        reservationDate,
                        ReservationStatus.CANCELED
                );

        List<BlockedTime> blockedTimes = blockedTimeRepository.findByBlockedDateOrderByStartTimeAsc(reservationDate);

        DayOfWeek dayOfWeek = reservationDate.getDayOfWeek();

        BusinessHour businessHour = businessHourRepository.findByDayOfWeek(dayOfWeek)
                .orElseThrow(() -> new IllegalArgumentException("영업시간 정보가 없습니다."));

        if (businessHour.getClosed()
                || businessHour.getOpenTime() == null
                || businessHour.getCloseTime() == null) {
            return List.of();
        }

        LocalTime start = businessHour.getOpenTime();
        LocalTime end = businessHour.getCloseTime().minusMinutes(menu.getDurationMinutes());

        List<LocalTime> timeSlots = new ArrayList<>();

        while (!start.isAfter(end)) {
            timeSlots.add(start);
            start = start.plusMinutes(30);
        }

        return timeSlots.stream()
                .filter(time -> !isOverlapped(time, menu.getDurationMinutes(), reservations))
                .filter(time -> !isBlocked(time, menu.getDurationMinutes(), blockedTimes))
                .map(time -> time.toString().substring(0, 5))
                .toList();
    }

    private boolean isBlocked(
            LocalTime newStartTime,
            Integer newDurationMinutes,
            List<BlockedTime> blockedTimes
    ) {
        LocalTime newEndTime = newStartTime.plusMinutes(newDurationMinutes);

        for (BlockedTime blockedTime : blockedTimes) {
            boolean isOverlapped =
                    newStartTime.isBefore(blockedTime.getEndTime())
                            && newEndTime.isAfter(blockedTime.getStartTime());

            if (isOverlapped) {
                return true;
            }
        }

        return false;
    }

    private boolean isOverlapped(
            LocalTime newStartTime,
            Integer newDurationMinutes,
            List<Reservation> reservations
    ) {
        LocalTime newEndTime = newStartTime.plusMinutes(newDurationMinutes);

        for (Reservation reservation : reservations) {
            LocalTime existingStartTime = reservation.getReservationTime();
            LocalTime existingEndTime = existingStartTime.plusMinutes(reservation.getDurationMinutes());

            if (newStartTime.isBefore(existingEndTime) && newEndTime.isAfter(existingStartTime)) {
                return true;
            }
        }

        return false;
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDto> getMyReservations(Long userId) {
        List<Reservation> reservations =
                reservationRepository.findAllByUser_IdOrderByReservationDateDescReservationTimeDesc(userId);

        return reservations.stream()
                .map(ReservationResponseDto::from)
                .toList();
    }
}