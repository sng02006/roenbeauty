package com.example.roenbeauty.blockedtime.repository;

import com.example.roenbeauty.blockedtime.entity.BlockedTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BlockedTimeRepository extends JpaRepository<BlockedTime, Long> {

    List<BlockedTime> findByBlockedDateOrderByStartTimeAsc(LocalDate blockedDate);
}