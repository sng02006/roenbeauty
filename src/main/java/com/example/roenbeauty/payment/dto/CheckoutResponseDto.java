package com.example.roenbeauty.payment.dto;

public class CheckoutResponseDto {

    private Long reservationId;
    private Long paymentId;
    private String orderId;
    private String orderName;
    private Integer amount;
    private String customerName;
    private String customerEmail;

    public CheckoutResponseDto(
            Long reservationId,
            Long paymentId,
            String orderId,
            String orderName,
            Integer amount,
            String customerName,
            String customerEmail
    ) {
        this.reservationId = reservationId;
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.orderName = orderName;
        this.amount = amount;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }
}