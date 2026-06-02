package com.example.roenbeauty.payment.dto;

public class TossPaymentConfirmResponseDto {

    private String paymentKey;
    private String orderId;
    private String method;
    private Integer totalAmount;
    private String status;

    protected TossPaymentConfirmResponseDto() {
    }

    public String getPaymentKey() {
        return paymentKey;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getMethod() {
        return method;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }
}