package com.example.roenbeauty.reservation.repository;

import com.example.roenbeauty.reservation.entity.Reservation;
import com.example.roenbeauty.reservation.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByOrderByReservationDateAscReservationTimeAsc();

    List<Reservation> findByStatusOrderByReservationDateAscReservationTimeAsc(ReservationStatus status);

    List<Reservation> findByReservationDateOrderByReservationTimeAsc(LocalDate reservationDate);

    List<Reservation> findByStatusAndReservationDateOrderByReservationTimeAsc(
            ReservationStatus status,
            LocalDate reservationDate
    );
}