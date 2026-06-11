package com.example.roenbeauty.reservation.repository;

import com.example.roenbeauty.reservation.entity.ReservationNotice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationNoticeRepository extends JpaRepository<ReservationNotice, Long> {
}