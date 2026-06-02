package com.example.roenbeauty.payment.dto;

import com.example.roenbeauty.payment.entity.Payment;
import com.example.roenbeauty.payment.enums.PaymentStatus;

public class PaymentResponseDto {

    private Long paymentId;
    private Long reservationId;
    private String paymentKey;
    private String orderId;
    private Integer amount;
    private String method;
    private PaymentStatus status;

    public PaymentResponseDto(
            Long paymentId,
            Long reservationId,
            String paymentKey,
            String orderId,
            Integer amount,
            String method,
            PaymentStatus status
    ) {
        this.paymentId = paymentId;
        this.reservationId = reservationId;
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.status = status;
    }

    public static PaymentResponseDto from(Payment payment) {
        return new PaymentResponseDto(
                payment.getId(),
                payment.getReservation().getId(),
                payment.getPaymentKey(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus()
        );
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public String getPaymentKey() {
        return paymentKey;
    }

    public String getOrderId() {
        return orderId;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getMethod() {
        return method;
    }

    public PaymentStatus getStatus() {
        return status;
    }
}