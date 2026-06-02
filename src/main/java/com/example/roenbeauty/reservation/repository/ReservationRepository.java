package com.example.roenbeauty.reservation.repository;

import com.example.roenbeauty.reservation.entity.Reservation;
import com.example.roenbeauty.reservation.enums.ReservationStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByOrderByReservationDateAscReservationTimeAsc();

    List<Reservation> findByStatusOrderByReservationDateAscReservationTimeAsc(ReservationStatus status);

    List<Reservation> findByReservationDateOrderByReservationTimeAsc(LocalDate reservationDate);

    List<Reservation> findByStatusAndReservationDateOrderByReservationTimeAsc(
            ReservationStatus status,
            LocalDate reservationDate
    );

    List<Reservation> findByReservationDateAndStatusNotOrderByReservationTimeAsc(
        LocalDate reservationDate,
        ReservationStatus status
    );

    boolean existsByReservationDateAndReservationTimeAndStatusNot(
            LocalDate reservationDate,
            LocalTime reservationTime,
            ReservationStatus status
    );

    List<Reservation> findByReservationDateAndStatusNot(
            LocalDate reservationDate,
            ReservationStatus status
    );

    List<Reservation> findAllByUser_IdOrderByReservationDateDescReservationTimeDesc(Long userId);

    List<Reservation> findByStatusAndCreatedAtBefore(
            ReservationStatus status,
            LocalDateTime createdAt
    );
}