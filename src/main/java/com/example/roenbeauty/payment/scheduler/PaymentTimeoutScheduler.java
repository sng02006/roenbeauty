package com.example.roenbeauty.payment.scheduler;

import com.example.roenbeauty.payment.entity.Payment;
import com.example.roenbeauty.payment.enums.PaymentStatus;
import com.example.roenbeauty.payment.repository.PaymentRepository;
import com.example.roenbeauty.reservation.entity.Reservation;
import com.example.roenbeauty.reservation.enums.CanceledBy;
import com.example.roenbeauty.reservation.enums.ReservationStatus;
import com.example.roenbeauty.reservation.repository.ReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PaymentTimeoutScheduler {

    private static final int PAYMENT_TIMEOUT_MINUTES = 10;

    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;

    public PaymentTimeoutScheduler(
            ReservationRepository reservationRepository,
            PaymentRepository paymentRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.paymentRepository = paymentRepository;
    }

    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void cancelExpiredWaitingPayments() {
        LocalDateTime expiredAt = LocalDateTime.now().minusMinutes(PAYMENT_TIMEOUT_MINUTES);

        List<Reservation> expiredReservations =
                reservationRepository.findByStatusAndCreatedAtBefore(
                        ReservationStatus.WAITING_PAYMENT,
                        expiredAt
                );

        for (Reservation reservation : expiredReservations) {
            Payment payment = paymentRepository.findByReservation(reservation)
                    .orElse(null);

            if (payment != null && payment.getStatus() == PaymentStatus.READY) {
                payment.fail();
            }

            reservation.cancel(
                    CanceledBy.SYSTEM,
                    "결제 시간 초과"
            );
        }
    }
}