package com.example.roenbeauty.reservation.repository;

import com.example.roenbeauty.reservation.entity.Reservation;
import com.example.roenbeauty.reservation.enums.ReservationStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

        Page<Reservation> findAllByOrderByReservationDateDescReservationTimeDesc(Pageable pageable);

        Page<Reservation> findByStatusOrderByReservationDateDescReservationTimeDesc(
                ReservationStatus status,
                Pageable pageable
        );

        Page<Reservation> findByReservationDateOrderByReservationTimeAsc(
                LocalDate reservationDate,
                Pageable pageable
        );

        Page<Reservation> findByStatusAndReservationDateOrderByReservationTimeAsc(
                ReservationStatus status,
                LocalDate reservationDate,
                Pageable pageable
        );

        @Query("""
                        SELECT r
                        FROM Reservation r
                        WHERE (:status IS NULL OR r.status = :status)
                        AND (:reservationDate IS NULL OR r.reservationDate = :reservationDate)
                        AND (
                                :keyword IS NULL
                                OR :keyword = ''
                                OR REPLACE(r.name, ' ', '') LIKE CONCAT('%', :keyword, '%')
                                OR REPLACE(r.phone, '-', '') LIKE CONCAT('%', :keyword, '%')
                        )
                        ORDER BY r.reservationDate DESC, r.reservationTime DESC
                        """)
                Page<Reservation> searchReservations(
                        @Param("status") ReservationStatus status,
                        @Param("reservationDate") LocalDate reservationDate,
                        @Param("keyword") String keyword,
                        Pageable pageable
                );

        List<Reservation> findAllByOrderByReservationDateAscReservationTimeAsc();

        @Query("""
                SELECT r
                FROM Reservation r
                WHERE r.user.id = :userId
                ORDER BY
                CASE r.status
                        WHEN 'WAITING_PAYMENT' THEN 1
                        WHEN 'PAID' THEN 2
                        WHEN 'CONFIRMED' THEN 3
                        WHEN 'COMPLETED' THEN 4
                        WHEN 'CANCELED' THEN 4
                        ELSE 5
                END ASC,
                r.reservationDate DESC,
                r.reservationTime DESC
                """)
        Page<Reservation> findMyReservationsOrderByPriority(
                @Param("userId") Long userId,
                Pageable pageable
        );

        List<Reservation> findByStatusOrderByReservationDateAscReservationTimeAsc(ReservationStatus status);

        List<Reservation> findByReservationDateOrderByReservationTimeAsc(LocalDate reservationDate);

        List<Reservation> findByStatusAndReservationDateOrderByReservationTimeAsc(
                ReservationStatus status,
                LocalDate reservationDate
        );

        List<Reservation> findByReservationDateAndStatusNotOrderByReservationTimeAsc(
                LocalDate reservationDate,
                ReservationStatus status
        );

        boolean existsByReservationDateAndReservationTimeAndStatusNot(
                LocalDate reservationDate,
                LocalTime reservationTime,
                ReservationStatus status
        );

        List<Reservation> findByReservationDateAndStatusNot(
                LocalDate reservationDate,
                ReservationStatus status
        );

        List<Reservation> findAllByUser_IdOrderByReservationDateDescReservationTimeDesc(Long userId);

        List<Reservation> findByStatusAndCreatedAtBefore(
                ReservationStatus status,
                LocalDateTime createdAt
        );
}