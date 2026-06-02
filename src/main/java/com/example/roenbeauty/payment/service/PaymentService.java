package com.example.roenbeauty.payment.service;

import com.example.roenbeauty.payment.client.TossPaymentClient;
import com.example.roenbeauty.payment.dto.PaymentCancelRequestDto;
import com.example.roenbeauty.payment.dto.PaymentConfirmRequestDto;
import com.example.roenbeauty.payment.dto.PaymentResponseDto;
import com.example.roenbeauty.payment.dto.TossPaymentConfirmResponseDto;
import com.example.roenbeauty.payment.entity.Payment;
import com.example.roenbeauty.payment.enums.PaymentStatus;
import com.example.roenbeauty.payment.repository.PaymentRepository;
import com.example.roenbeauty.reservation.entity.Reservation;
import com.example.roenbeauty.reservation.enums.ReservationStatus;
import com.example.roenbeauty.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final TossPaymentClient tossPaymentClient;

    public PaymentService(
            PaymentRepository paymentRepository,
            ReservationRepository reservationRepository,
            TossPaymentClient tossPaymentClient
    ) {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
        this.tossPaymentClient = tossPaymentClient;
    }

    @Transactional
    public PaymentResponseDto confirmPayment(Long reservationId, PaymentConfirmRequestDto requestDto) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약이 존재하지 않습니다."));

        if (reservation.getStatus() != ReservationStatus.WAITING_PAYMENT) {
            throw new IllegalArgumentException("결제 대기 상태의 예약만 결제할 수 있습니다.");
        }

        Payment payment = paymentRepository.findByReservation(reservation)
                .orElseThrow(() -> new IllegalArgumentException("결제 정보가 존재하지 않습니다."));

        if (payment.getStatus() == PaymentStatus.DONE) {
            throw new IllegalArgumentException("이미 결제가 완료된 예약입니다.");
        }

        if (!payment.getOrderId().equals(requestDto.getOrderId())) {
            throw new IllegalArgumentException("주문번호가 일치하지 않습니다.");
        }

        if (!payment.getAmount().equals(requestDto.getAmount())) {
            throw new IllegalArgumentException("결제 금액이 일치하지 않습니다.");
        }

        TossPaymentConfirmResponseDto tossResponse =
                tossPaymentClient.confirm(requestDto);

        if (tossResponse == null || !"DONE".equals(tossResponse.getStatus())) {
            payment.fail();
            throw new IllegalArgumentException("결제 승인에 실패했습니다.");
        }

        payment.complete(
                tossResponse.getPaymentKey(),
                tossResponse.getMethod()
        );

        reservation.updateStatus(ReservationStatus.PAID);

        return PaymentResponseDto.from(payment);
    }

    @Transactional(readOnly = true)
    public PaymentResponseDto getPayment(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약이 존재하지 않습니다."));

        Payment payment = paymentRepository.findByReservation(reservation)
                .orElseThrow(() -> new IllegalArgumentException("결제 정보가 존재하지 않습니다."));

        return PaymentResponseDto.from(payment);
    }

    @Transactional
    public PaymentResponseDto cancelPayment(Long reservationId, PaymentCancelRequestDto requestDto) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약이 존재하지 않습니다."));

        if (reservation.getStatus() == ReservationStatus.CANCELED) {
            throw new IllegalArgumentException("이미 취소된 예약입니다.");
        }

        if (reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new IllegalArgumentException("이미 완료된 예약은 취소할 수 없습니다.");
        }

        Payment payment = paymentRepository.findByReservation(reservation)
                .orElseThrow(() -> new IllegalArgumentException("결제 정보가 존재하지 않습니다."));

        String cancelReason = requestDto.getCancelReason();

        if (cancelReason == null || cancelReason.isBlank()) {
            cancelReason = "예약 취소";
        }

        if (payment.getStatus() == PaymentStatus.DONE) {
            if (payment.getPaymentKey() == null || payment.getPaymentKey().isBlank()) {
                throw new IllegalArgumentException("결제 취소에 필요한 paymentKey가 없습니다.");
            }

            TossPaymentConfirmResponseDto tossResponse =
                    tossPaymentClient.cancel(payment.getPaymentKey(), cancelReason);

            if (tossResponse == null || !"CANCELED".equals(tossResponse.getStatus())) {
                throw new IllegalArgumentException("결제 취소에 실패했습니다.");
            }

            payment.cancel(cancelReason);
        } else if (payment.getStatus() == PaymentStatus.READY) {
            payment.fail();
        } else if (payment.getStatus() == PaymentStatus.CANCELED) {
            throw new IllegalArgumentException("이미 취소된 결제입니다.");
        } else if (payment.getStatus() == PaymentStatus.FAILED) {
            throw new IllegalArgumentException("이미 실패 처리된 결제입니다.");
        }

        reservation.cancel(
                requestDto.getCanceledBy(),
                cancelReason
        );

        return PaymentResponseDto.from(payment);
    }
}