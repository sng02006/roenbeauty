package com.example.roenbeauty.payment.entity;

import com.example.roenbeauty.payment.enums.PaymentStatus;
import com.example.roenbeauty.reservation.entity.Reservation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentKey;

    @Column(nullable = false, unique = true)
    private String orderId;

    @Column(nullable = false)
    private Integer amount;

    private String method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    private String cancelReason;

    private LocalDateTime canceledAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false, unique = true)
    private Reservation reservation;

    public Payment(Reservation reservation, String orderId, Integer amount) {
        this.reservation = reservation;
        this.orderId = orderId;
        this.amount = amount;
        this.status = PaymentStatus.READY;
    }

    public void complete(String paymentKey, String method) {
        this.paymentKey = paymentKey;
        this.method = method;
        this.status = PaymentStatus.DONE;
    }

    public void fail() {
        this.status = PaymentStatus.FAILED;
    }

    public void cancel(String cancelReason) {
        this.status = PaymentStatus.CANCELED;
        this.cancelReason = cancelReason;
        this.canceledAt = LocalDateTime.now();
    }
}