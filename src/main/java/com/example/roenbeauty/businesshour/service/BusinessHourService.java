package com.example.roenbeauty.businesshour.service;

import com.example.roenbeauty.businesshour.dto.BusinessHourExceptionRequestDto;
import com.example.roenbeauty.businesshour.dto.BusinessHourResponseDto;
import com.example.roenbeauty.businesshour.dto.BusinessHourUpdateRequestDto;
import com.example.roenbeauty.businesshour.entity.BusinessHour;
import com.example.roenbeauty.businesshour.entity.BusinessHourException;
import com.example.roenbeauty.businesshour.repository.BusinessHourExceptionRepository;
import com.example.roenbeauty.businesshour.repository.BusinessHourRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BusinessHourService {

    private final BusinessHourRepository businessHourRepository;
    private final BusinessHourExceptionRepository exceptionRepository;

    public BusinessHourService(
            BusinessHourRepository businessHourRepository,
            BusinessHourExceptionRepository exceptionRepository
    ) {
        this.businessHourRepository = businessHourRepository;
        this.exceptionRepository = exceptionRepository;
    }

    @Transactional(readOnly = true)
    public BusinessHourResponseDto getByDate(LocalDate date) {
        return exceptionRepository.findByDate(date)
                .map(BusinessHourResponseDto::from)
                .orElseGet(() -> {
                    BusinessHour businessHour = businessHourRepository.findByDayOfWeek(date.getDayOfWeek())
                            .orElseThrow(() -> new IllegalStateException("기본 영업시간이 존재하지 않습니다."));
                    return BusinessHourResponseDto.from(businessHour);
                });
    }

    @Transactional(readOnly = true)
    public List<BusinessHourResponseDto> getAllDefaultBusinessHours() {
        return businessHourRepository.findAll()
                .stream()
                .map(BusinessHourResponseDto::from)
                .toList();
    }

    @Transactional
    public void updateDefaultBusinessHour(BusinessHourUpdateRequestDto request) {
        BusinessHour businessHour = businessHourRepository.findByDayOfWeek(request.getDayOfWeek())
                .orElseThrow(() -> new IllegalStateException("기본 영업시간이 존재하지 않습니다."));

        businessHour.update(
                request.getOpenTime(),
                request.getCloseTime(),
                request.getClosed()
        );
    }

    @Transactional
    public void upsertException(BusinessHourExceptionRequestDto request) {
        BusinessHourException exception = exceptionRepository.findByDate(request.getDate())
                .orElseGet(() -> new BusinessHourException(
                        request.getDate(),
                        request.getOpenTime(),
                        request.getCloseTime(),
                        request.getClosed()
                ));

        exception.update(
                request.getOpenTime(),
                request.getCloseTime(),
                request.getClosed()
        );

        exceptionRepository.save(exception);
    }
}