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
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;

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

    @Transactional(readOnly = true)
    public List<BusinessHourResponseDto> getAllExceptions() {
        return exceptionRepository.findAll()
                .stream()
                .map(BusinessHourResponseDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<String> getHolidayDates(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<BusinessHourException> exceptions =
                exceptionRepository.findByDateBetweenAndClosedTrue(startDate, endDate);

        Set<LocalDate> holidayDates = exceptions.stream()
                .map(BusinessHourException::getDate)
                .collect(Collectors.toSet());

        List<BusinessHour> defaultBusinessHours = businessHourRepository.findAll();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            LocalDate currentDate = date;

            boolean isDefaultClosed = defaultBusinessHours.stream()
                    .anyMatch(businessHour ->
                            businessHour.getDayOfWeek() == currentDate.getDayOfWeek()
                                    && Boolean.TRUE.equals(businessHour.getClosed())
                    );

            boolean hasOpenException = exceptionRepository.findByDate(currentDate)
                    .map(exception -> !Boolean.TRUE.equals(exception.getClosed()))
                    .orElse(false);

            if (isDefaultClosed && !hasOpenException) {
                holidayDates.add(currentDate);
            }
        }

        return holidayDates.stream()
                .sorted()
                .map(LocalDate::toString)
                .toList();
    }
}