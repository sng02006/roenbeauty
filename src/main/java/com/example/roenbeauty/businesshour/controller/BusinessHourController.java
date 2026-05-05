package com.example.roenbeauty.businesshour.controller;

import com.example.roenbeauty.businesshour.dto.BusinessHourExceptionRequestDto;
import com.example.roenbeauty.businesshour.dto.BusinessHourResponseDto;
import com.example.roenbeauty.businesshour.dto.BusinessHourUpdateRequestDto;
import com.example.roenbeauty.businesshour.service.BusinessHourService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/business-hours")
public class BusinessHourController {

    private final BusinessHourService service;

    public BusinessHourController(BusinessHourService service) {
        this.service = service;
    }

    @GetMapping
    public BusinessHourResponseDto getBusinessHour(@RequestParam("date") String date) {
        return service.getByDate(LocalDate.parse(date));
    }

    @GetMapping("/default")
    public List<BusinessHourResponseDto> getDefaultBusinessHours() {
        return service.getAllDefaultBusinessHours();
    }

    @PutMapping("/default")
    public void updateDefaultBusinessHour(@RequestBody BusinessHourUpdateRequestDto request) {
        service.updateDefaultBusinessHour(request);
    }

    @PostMapping("/exceptions")
    public void upsertException(@RequestBody BusinessHourExceptionRequestDto request) {
        service.upsertException(request);
    }

    @GetMapping("/exceptions")
    public List<BusinessHourResponseDto> getBusinessHourExceptions() {
        return service.getAllExceptions();
    }
}