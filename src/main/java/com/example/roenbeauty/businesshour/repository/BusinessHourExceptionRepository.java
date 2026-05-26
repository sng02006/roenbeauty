package com.example.roenbeauty.businesshour.repository;

import com.example.roenbeauty.businesshour.entity.BusinessHourException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BusinessHourExceptionRepository extends JpaRepository<BusinessHourException, Long> {

    Optional<BusinessHourException> findByDate(LocalDate date);

    List<BusinessHourException> findByDateBetweenAndClosedTrue(LocalDate startDate, LocalDate endDate);
}