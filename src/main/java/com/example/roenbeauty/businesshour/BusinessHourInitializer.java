package com.example.roenbeauty.businesshour;

import com.example.roenbeauty.businesshour.entity.BusinessHour;
import com.example.roenbeauty.businesshour.repository.BusinessHourRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Component
public class BusinessHourInitializer {

    private final BusinessHourRepository repository;

    public BusinessHourInitializer(BusinessHourRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        if (repository.count() > 0) return;

        for (DayOfWeek day : DayOfWeek.values()) {
            if (day == DayOfWeek.SUNDAY) {
                repository.save(new BusinessHour(day, null, null, true));
            } else {
                repository.save(new BusinessHour(
                        day,
                        LocalTime.of(11, 0),
                        LocalTime.of(21, 0),
                        false
                ));
            }
        }
    }
}