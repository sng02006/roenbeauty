package com.example.roenbeauty.reservation.entity;

import com.example.roenbeauty.global.common.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "reservation_notices")
public class ReservationNotice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    protected ReservationNotice() {
    }

    public ReservationNotice(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}