package com.example.roenbeauty.reservation.service;

import com.example.roenbeauty.blockedtime.entity.BlockedTime;
import com.example.roenbeauty.blockedtime.repository.BlockedTimeRepository;
import com.example.roenbeauty.businesshour.dto.BusinessHourResponseDto;
import com.example.roenbeauty.businesshour.service.BusinessHourService;
import com.example.roenbeauty.global.dto.AuthUser;
import com.example.roenbeauty.menu.entity.Menu;
import com.example.roenbeauty.menu.repository.MenuRepository;
import com.example.roenbeauty.payment.dto.CheckoutResponseDto;
import com.example.roenbeauty.payment.entity.Payment;
import com.example.roenbeauty.payment.repository.PaymentRepository;
import com.example.roenbeauty.reservation.dto.ReservationCreateRequestDto;
import com.example.roenbeauty.reservation.dto.ReservationResponseDto;
import com.example.roenbeauty.reservation.dto.ReservationUpdateStatusRequestDto;
import com.example.roenbeauty.reservation.entity.Reservation;
import com.example.roenbeauty.reservation.enums.ReservationStatus;
import com.example.roenbeauty.reservation.repository.ReservationRepository;
import com.example.roenbeauty.user.entity.User;
import com.example.roenbeauty.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    private static final int DEPOSIT_AMOUNT = 20000;
    private static final int NIGHT_SURCHARGE_AMOUNT = 10000;
    private static final int NIGHT_SURCHARGE_START_HOUR = 20;

    private final ReservationRepository reservationRepository;
    private final MenuRepository menuRepository;
    private final BusinessHourService businessHourService;
    private final BlockedTimeRepository blockedTimeRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            MenuRepository menuRepository,
            BusinessHourService businessHourService,
            BlockedTimeRepository blockedTimeRepository,
            UserRepository userRepository,
            PaymentRepository paymentRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.menuRepository = menuRepository;
        this.businessHourService = businessHourService;
        this.blockedTimeRepository = blockedTimeRepository;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
    }

        @Transactional
        public CheckoutResponseDto createReservation(
                AuthUser authUser,
                ReservationCreateRequestDto requestDto
        ) {
        Menu menu = menuRepository.findById(requestDto.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        validateReservationDate(requestDto.getReservationDate());

        validateReservationTime(
                requestDto.getReservationDate(),
                requestDto.getReservationTime(),
                menu.getDurationMinutes()
        );

        Reservation reservation = new Reservation(
                requestDto.getName(),
                requestDto.getPhone(),
                requestDto.getReservationDate(),
                requestDto.getReservationTime(),
                menu.getName(),
                requestDto.getRequestMemo(),
                ReservationStatus.WAITING_PAYMENT,
                menu.getDurationMinutes(),
                user
        );

        Reservation savedReservation = reservationRepository.save(reservation);

        String orderId = "reservation-" + savedReservation.getId();
        String orderName = "Ro:en Beauty 예약금";
        int paymentAmount = calculateDepositAmount(savedReservation.getReservationTime());

        Payment payment = new Payment(
                savedReservation,
                orderId,
                paymentAmount
        );

        Payment savedPayment = paymentRepository.save(payment);

        return new CheckoutResponseDto(
                savedReservation.getId(),
                savedPayment.getId(),
                savedPayment.getOrderId(),
                orderName,
                savedPayment.getAmount(),
                savedReservation.getName(),
                savedReservation.getUser().getEmail()
        );
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDto> getReservations(
            ReservationStatus status,
            LocalDate reservationDate,
            String keyword
    ) {
        return reservationRepository
                .findAllByOrderByReservationDateAscReservationTimeAsc()
                .stream()
                .filter(reservation ->
                        status == null ||
                        reservation.getStatus() == status
                )
                .filter(reservation ->
                        reservationDate == null ||
                        reservation.getReservationDate().equals(reservationDate)
                )
                .filter(reservation -> {
                    if (keyword == null || keyword.isBlank()) {
                        return true;
                    }

                    String normalizedKeyword = keyword.replaceAll("\\s", "");
                    String normalizedName = reservation.getName().replaceAll("\\s", "");

                    String numberKeyword = keyword.replaceAll("\\D", "");
                    String normalizedPhone = reservation.getPhone().replaceAll("\\D", "");

                    boolean nameMatched = normalizedName.contains(normalizedKeyword);
                    boolean phoneMatched = !numberKeyword.isBlank()
                            && normalizedPhone.contains(numberKeyword);

                    return nameMatched || phoneMatched;
                })
                .map(ReservationResponseDto::from)
                .toList();
    }

    @Transactional
    public ReservationResponseDto updateReservationStatus(Long id, ReservationUpdateStatusRequestDto requestDto) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약이 존재하지 않습니다."));

        validateStatusChange(reservation.getStatus(), requestDto.getStatus());

        reservation.updateStatus(requestDto.getStatus());

        return ReservationResponseDto.from(reservation);
    }

    @Transactional(readOnly = true)
    public List<String> getReservedTimes(LocalDate reservationDate) {
        return reservationRepository
                .findByReservationDateAndStatusNotOrderByReservationTimeAsc(
                        reservationDate,
                        ReservationStatus.CANCELED
                )
                .stream()
                .map(reservation -> reservation.getReservationTime().toString())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<String> getAvailableTimes(LocalDate reservationDate, Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

        List<Reservation> reservations = reservationRepository
                .findByReservationDateAndStatusNot(
                        reservationDate,
                        ReservationStatus.CANCELED
                );

        List<BlockedTime> blockedTimes =
                blockedTimeRepository.findByBlockedDateOrderByStartTimeAsc(reservationDate);

        BusinessHourResponseDto businessHour = businessHourService.getByDate(reservationDate);

        if (businessHour.getClosed()
                || businessHour.getOpenTime() == null
                || businessHour.getCloseTime() == null) {
            return List.of();
        }

        LocalTime start = businessHour.getOpenTime();
        LocalTime end = businessHour.getCloseTime().minusMinutes(menu.getDurationMinutes());

        List<LocalTime> timeSlots = new ArrayList<>();

        while (!start.isAfter(end)) {
            timeSlots.add(start);
            start = start.plusMinutes(30);
        }

        return timeSlots.stream()
                .filter(time -> !isOverlapped(time, menu.getDurationMinutes(), reservations))
                .filter(time -> !isBlocked(time, menu.getDurationMinutes(), blockedTimes))
                .map(time -> time.toString().substring(0, 5))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDto> getMyReservations(AuthUser authUser) {
        List<Reservation> reservations =
                reservationRepository.findAllByUser_IdOrderByReservationDateDescReservationTimeDesc(
                        authUser.getUserId()
                );

        return reservations.stream()
                .map(ReservationResponseDto::from)
                .toList();
    }

    private int calculateDepositAmount(LocalTime reservationTime) {
        int amount = DEPOSIT_AMOUNT;

        if (reservationTime.getHour() >= NIGHT_SURCHARGE_START_HOUR) {
            amount += NIGHT_SURCHARGE_AMOUNT;
        }

        return amount;
    }

    private void validateReservationDate(LocalDate reservationDate) {
        if (reservationDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("지난 날짜는 예약할 수 없습니다.");
        }
    }

    private void validateReservationTime(
            LocalDate reservationDate,
            LocalTime newStartTime,
            Integer newDurationMinutes
    ) {
        LocalTime newEndTime = newStartTime.plusMinutes(newDurationMinutes);

        BusinessHourResponseDto businessHour = businessHourService.getByDate(reservationDate);

        if (businessHour.getClosed()
                || businessHour.getOpenTime() == null
                || businessHour.getCloseTime() == null) {
            throw new IllegalArgumentException("해당 날짜는 예약할 수 없습니다.");
        }

        if (newStartTime.isBefore(businessHour.getOpenTime())
                || newEndTime.isAfter(businessHour.getCloseTime())) {
            throw new IllegalArgumentException("영업시간 내에서만 예약할 수 있습니다.");
        }

        List<Reservation> reservations = reservationRepository
                .findByReservationDateAndStatusNot(
                        reservationDate,
                        ReservationStatus.CANCELED
                );

        if (isOverlapped(newStartTime, newDurationMinutes, reservations)) {
            throw new IllegalArgumentException("이미 예약된 시간과 겹칩니다. 다른 시간을 선택해주세요.");
        }

        List<BlockedTime> blockedTimes =
                blockedTimeRepository.findByBlockedDateOrderByStartTimeAsc(reservationDate);

        if (isBlocked(newStartTime, newDurationMinutes, blockedTimes)) {
            throw new IllegalArgumentException("예약이 불가능한 시간입니다. 다른 시간을 선택해주세요.");
        }
    }

    private void validateStatusChange(ReservationStatus current, ReservationStatus target) {
        if (current == ReservationStatus.CANCELED || current == ReservationStatus.COMPLETED) {
            throw new IllegalArgumentException("이미 종료된 예약은 상태 변경이 불가능합니다.");
        }

        if (current == ReservationStatus.WAITING_PAYMENT) {
            if (target != ReservationStatus.PAID && target != ReservationStatus.CANCELED) {
                throw new IllegalArgumentException("결제 대기 상태에서는 결제 완료 또는 취소만 가능합니다.");
            }
        }

        if (current == ReservationStatus.PAID) {
            if (target != ReservationStatus.CONFIRMED && target != ReservationStatus.CANCELED) {
                throw new IllegalArgumentException("결제 완료 상태에서는 확정 또는 취소만 가능합니다.");
            }
        }

        if (current == ReservationStatus.CONFIRMED) {
            if (target != ReservationStatus.COMPLETED && target != ReservationStatus.CANCELED) {
                throw new IllegalArgumentException("확정 상태에서는 완료 또는 취소만 가능합니다.");
            }
        }
    }

    private boolean isBlocked(
            LocalTime newStartTime,
            Integer newDurationMinutes,
            List<BlockedTime> blockedTimes
    ) {
        LocalTime newEndTime = newStartTime.plusMinutes(newDurationMinutes);

        for (BlockedTime blockedTime : blockedTimes) {
            boolean isOverlapped =
                    newStartTime.isBefore(blockedTime.getEndTime())
                            && newEndTime.isAfter(blockedTime.getStartTime());

            if (isOverlapped) {
                return true;
            }
        }

        return false;
    }

    private boolean isOverlapped(
            LocalTime newStartTime,
            Integer newDurationMinutes,
            List<Reservation> reservations
    ) {
        LocalTime newEndTime = newStartTime.plusMinutes(newDurationMinutes);

        for (Reservation reservation : reservations) {
            LocalTime existingStartTime = reservation.getReservationTime();
            LocalTime existingEndTime = existingStartTime.plusMinutes(reservation.getDurationMinutes());

            if (newStartTime.isBefore(existingEndTime)
                    && newEndTime.isAfter(existingStartTime)) {
                return true;
            }
        }

        return false;
    }
}