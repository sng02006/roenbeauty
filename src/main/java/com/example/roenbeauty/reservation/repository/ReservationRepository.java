package com.example.roenbeauty.reservation.repository;

import com.example.roenbeauty.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}