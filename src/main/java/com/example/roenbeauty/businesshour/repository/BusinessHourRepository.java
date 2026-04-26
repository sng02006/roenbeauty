package com.example.roenbeauty.businesshour.repository;

import com.example.roenbeauty.businesshour.entity.BusinessHour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.Optional;

public interface BusinessHourRepository extends JpaRepository<BusinessHour, Long> {

    Optional<BusinessHour> findByDayOfWeek(DayOfWeek dayOfWeek);
}