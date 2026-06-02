package com.example.roenbeauty.payment.repository;

import com.example.roenbeauty.payment.entity.Payment;
import com.example.roenbeauty.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByReservation(Reservation reservation);

    Optional<Payment> findByOrderId(String orderId);

    boolean existsByReservation(Reservation reservation);
}