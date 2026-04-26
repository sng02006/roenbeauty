package com.example.roenbeauty.blockedtime.service;

import com.example.roenbeauty.blockedtime.dto.BlockedTimeCreateRequestDto;
import com.example.roenbeauty.blockedtime.dto.BlockedTimeResponseDto;
import com.example.roenbeauty.blockedtime.entity.BlockedTime;
import com.example.roenbeauty.blockedtime.repository.BlockedTimeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BlockedTimeService {

    private final BlockedTimeRepository blockedTimeRepository;

    public BlockedTimeService(BlockedTimeRepository blockedTimeRepository) {
        this.blockedTimeRepository = blockedTimeRepository;
    }

    @Transactional
    public BlockedTimeResponseDto createBlockedTime(BlockedTimeCreateRequestDto requestDto) {
        if (!requestDto.getStartTime().isBefore(requestDto.getEndTime())) {
            throw new IllegalArgumentException("차단 시작 시간은 종료 시간보다 빨라야 합니다.");
        }

        BlockedTime blockedTime = new BlockedTime(
                requestDto.getBlockedDate(),
                requestDto.getStartTime(),
                requestDto.getEndTime(),
                requestDto.getReason()
        );

        return BlockedTimeResponseDto.from(blockedTimeRepository.save(blockedTime));
    }

    @Transactional(readOnly = true)
    public List<BlockedTimeResponseDto> getBlockedTimes(LocalDate blockedDate) {
        return blockedTimeRepository.findByBlockedDateOrderByStartTimeAsc(blockedDate)
                .stream()
                .map(BlockedTimeResponseDto::from)
                .toList();
    }

    @Transactional
    public void deleteBlockedTime(Long id) {
        if (!blockedTimeRepository.existsById(id)) {
            throw new IllegalArgumentException("존재하지 않는 차단 시간입니다.");
        }

        blockedTimeRepository.deleteById(id);
    }
}