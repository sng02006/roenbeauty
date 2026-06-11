package com.example.roenbeauty.reservation.service;

import com.example.roenbeauty.reservation.dto.ReservationNoticeResponseDto;
import com.example.roenbeauty.reservation.dto.ReservationNoticeUpdateRequestDto;
import com.example.roenbeauty.reservation.entity.ReservationNotice;
import com.example.roenbeauty.reservation.repository.ReservationNoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationNoticeService {

    private static final String DEFAULT_NOTICE_CONTENT = """
            예약 전 꼭 확인해주세요.

            - 예약금 20,000원이 발생합니다.
            - 20시 이후 예약 시 야간 추가금 10,000원이 발생합니다.
            - 예약금 결제 완료 후 예약 요청이 접수됩니다.
            - 예약 요청 후 매장 확인을 거쳐 최종 예약이 확정됩니다.
            - 시술 내용, 디자인, 연장 여부 등에 따라 소요 시간과 금액이 달라질 수 있습니다.
            - 예약 변경 및 취소는 가능한 한 미리 연락 부탁드립니다.
            - 당일 취소 또는 노쇼 발생 시 예약이 제한될 수 있습니다.
            """;

    private final ReservationNoticeRepository reservationNoticeRepository;

    public ReservationNoticeService(ReservationNoticeRepository reservationNoticeRepository) {
        this.reservationNoticeRepository = reservationNoticeRepository;
    }

    @Transactional
    public ReservationNoticeResponseDto getNotice() {
        ReservationNotice notice = getOrCreateNotice();

        return ReservationNoticeResponseDto.from(notice);
    }

    @Transactional
    public ReservationNoticeResponseDto updateNotice(ReservationNoticeUpdateRequestDto requestDto) {
        String content = requestDto.getContent();

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("예약 안내 문구를 입력해주세요.");
        }

        ReservationNotice notice = getOrCreateNotice();
        notice.updateContent(content);

        return ReservationNoticeResponseDto.from(notice);
    }

    private ReservationNotice getOrCreateNotice() {
        return reservationNoticeRepository.findAll()
                .stream()
                .findFirst()
                .orElseGet(() -> reservationNoticeRepository.save(
                        new ReservationNotice(DEFAULT_NOTICE_CONTENT)
                ));
    }
}