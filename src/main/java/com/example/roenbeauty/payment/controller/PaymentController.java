package com.example.roenbeauty.payment.controller;

import com.example.roenbeauty.global.dto.AuthUser;
import com.example.roenbeauty.payment.dto.CheckoutResponseDto;
import com.example.roenbeauty.payment.dto.PaymentCancelRequestDto;
import com.example.roenbeauty.payment.dto.PaymentConfirmRequestDto;
import com.example.roenbeauty.payment.dto.PaymentResponseDto;
import com.example.roenbeauty.payment.service.PaymentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable("reservationId") Long reservationId,
            @RequestBody PaymentConfirmRequestDto requestDto
    ) {
        return paymentService.confirmPayment(authUser, reservationId, requestDto);
    }

    @GetMapping
    public PaymentResponseDto getPayment(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable("reservationId") Long reservationId
    ) {
        return paymentService.getPayment(authUser, reservationId);
    }

    @GetMapping("/checkout")
    public CheckoutResponseDto getCheckoutInfo(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable("reservationId") Long reservationId
    ) {
        return paymentService.getCheckoutInfo(authUser, reservationId);
    }

    @PostMapping("/cancel")
    public PaymentResponseDto cancelPayment(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable("reservationId") Long reservationId,
            @RequestBody PaymentCancelRequestDto requestDto
    ) {
        return paymentService.cancelPayment(authUser, reservationId, requestDto);
    }
}