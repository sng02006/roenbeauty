package com.example.roenbeauty.reservation.dto;

import com.example.roenbeauty.reservation.entity.ReservationNotice;

public class ReservationNoticeResponseDto {

    private Long id;
    private String content;

    public ReservationNoticeResponseDto(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public static ReservationNoticeResponseDto from(ReservationNotice notice) {
        return new ReservationNoticeResponseDto(
                notice.getId(),
                notice.getContent()
        );
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}