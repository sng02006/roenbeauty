package com.example.roenbeauty.blockedtime.controller;

import com.example.roenbeauty.blockedtime.dto.BlockedTimeCreateRequestDto;
import com.example.roenbeauty.blockedtime.dto.BlockedTimeResponseDto;
import com.example.roenbeauty.blockedtime.service.BlockedTimeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/blocked-times")
public class BlockedTimeController {

    private final BlockedTimeService blockedTimeService;

    public BlockedTimeController(BlockedTimeService blockedTimeService) {
        this.blockedTimeService = blockedTimeService;
    }

    @PostMapping
    public BlockedTimeResponseDto createBlockedTime(@RequestBody BlockedTimeCreateRequestDto requestDto) {
        return blockedTimeService.createBlockedTime(requestDto);
    }

    @GetMapping
    public List<BlockedTimeResponseDto> getBlockedTimes(
            @RequestParam(name = "blockedDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate blockedDate
    ) {
        return blockedTimeService.getBlockedTimes(blockedDate);
    }

    @DeleteMapping("/{id}")
    public void deleteBlockedTime(@PathVariable("id") Long id) {
        blockedTimeService.deleteBlockedTime(id);
    }
}