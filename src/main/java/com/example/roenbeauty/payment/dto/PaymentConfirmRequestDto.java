package com.example.roenbeauty.payment.dto;

public class PaymentConfirmRequestDto {

    private String paymentKey;
    private String orderId;
    private Integer amount;

    protected PaymentConfirmRequestDto() {
    }

    public PaymentConfirmRequestDto(String paymentKey, String orderId, Integer amount) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
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
}