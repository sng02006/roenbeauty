package com.example.roenbeauty.reservation.enums;

public enum CanceledBy {
    CUSTOMER, // 고객에 의해 취소
    OWNER,    // 사장님에 의해 취소
    SYSTEM    // 시스템에 의해 취소 (예: 무단 결제 실패, 예약 시간 초과 등)
}