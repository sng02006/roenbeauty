package com.example.roenbeauty.payment.controller;

import com.example.roenbeauty.payment.dto.CheckoutResponseDto;
import com.example.roenbeauty.payment.dto.PaymentCancelRequestDto;
import com.example.roenbeauty.payment.dto.PaymentConfirmRequestDto;
import com.example.roenbeauty.payment.dto.PaymentResponseDto;
import com.example.roenbeauty.payment.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations/{reservationId}/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/confirm")
    public PaymentResponseDto confirmPayment(
            @PathVariable("reservationId") Long reservationId,
            @RequestBody PaymentConfirmRequestDto requestDto
    ) {
        return paymentService.confirmPayment(reservationId, requestDto);
    }

    @GetMapping
    public PaymentResponseDto getPayment(
            @PathVariable("reservationId") Long reservationId
    ) {
        return paymentService.getPayment(reservationId);
    }

    @GetMapping("/checkout")
    public CheckoutResponseDto getCheckoutInfo(
            @PathVariable("reservationId") Long reservationId
    ) {
        return paymentService.getCheckoutInfo(reservationId);
    }

    @PostMapping("/cancel")
    public PaymentResponseDto cancelPayment(
            @PathVariable("reservationId") Long reservationId,
            @RequestBody PaymentCancelRequestDto requestDto
    ) {
        return paymentService.cancelPayment(reservationId, requestDto);
    }
}