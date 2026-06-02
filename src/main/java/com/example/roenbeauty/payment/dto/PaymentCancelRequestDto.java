package com.example.roenbeauty.payment.dto;

import com.example.roenbeauty.reservation.enums.CanceledBy;

public class PaymentCancelRequestDto {

    private String cancelReason;
    private CanceledBy canceledBy;

    protected PaymentCancelRequestDto() {
    }

    public PaymentCancelRequestDto(String cancelReason, CanceledBy canceledBy) {
        this.cancelReason = cancelReason;
        this.canceledBy = canceledBy;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public CanceledBy getCanceledBy() {
        return canceledBy;
    }
}