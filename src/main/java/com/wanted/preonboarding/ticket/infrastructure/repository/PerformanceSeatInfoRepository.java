package com.wanted.preonboarding.ticket.infrastructure.repository;

import com.wanted.preonboarding.ticket.domain.entity.PerformanceSeatInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PerformanceSeatInfoRepository extends JpaRepository<PerformanceSeatInfo, Integer> {

    PerformanceSeatInfo findByPerformanceIdAndRoundAndLineAndSeat(UUID performanceId, int round, char line, int seat);

    List<PerformanceSeatInfo> findByPerformanceIdAndRoundAndIsReserve(UUID performanceId,int round, String isReserve);

    PerformanceSeatInfo findFirstByPerformanceIdAndRoundAndIsReserve(UUID performanceId,int round, String isReserve);
}
