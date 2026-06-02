package com.example.roenbeauty.reservation.enums;

public enum ReservationStatus {
    WAITING_PAYMENT, // 예약 생성, 결제 대기
    PAID,            // 결제 완료, 관리자 확인 대기
    CONFIRMED,       // 예약 확정
    COMPLETED,       // 방문 완료
    CANCELED         // 예약 취소
}