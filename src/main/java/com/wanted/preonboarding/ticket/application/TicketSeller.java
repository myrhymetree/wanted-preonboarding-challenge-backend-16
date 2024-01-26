package com.wanted.preonboarding.ticket.application;

import com.wanted.preonboarding.ticket.domain.dto.CancelInfo;
import com.wanted.preonboarding.ticket.domain.dto.PerformanceInfo;
import com.wanted.preonboarding.ticket.domain.dto.ReserveInfo;
import com.wanted.preonboarding.ticket.domain.entity.Performance;
import com.wanted.preonboarding.ticket.domain.entity.PerformanceSeatInfo;
import com.wanted.preonboarding.ticket.domain.entity.Reservation;
import com.wanted.preonboarding.ticket.infrastructure.repository.PerformanceRepository;
import com.wanted.preonboarding.ticket.infrastructure.repository.PerformanceSeatInfoRepository;
import com.wanted.preonboarding.ticket.infrastructure.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketSeller {
    private final PerformanceRepository performanceRepository;
    private final ReservationRepository reservationRepository;
    private final PerformanceSeatInfoRepository performanceSeatInfoRepository;
    private long totalAmount = 0L;

    public List<PerformanceInfo> getAllPerformanceInfoList() {
        return performanceRepository.findByIsReserve("enable")
            .stream()
            .map(PerformanceInfo::of)
            .toList();
    }

    public ReserveInfo getReserveInfoDetail(String name, String phone) {

        Reservation reservation = reservationRepository.findByNameAndPhoneNumber(name, phone);

        if(reservation != null) {
            Performance performance = performanceRepository.findById(reservation.getPerformanceId()).get();

            return ReserveInfo.builder()
                    .performanceId(performance.getId())
                    .performanceName(performance.getName())
                    .reservationName(reservation.getName())
                    .reservationPhoneNumber(reservation.getPhoneNumber())
                    .reservationStatus(performance.getIsReserve())
                    .round(performance.getRound())
                    .line(reservation.getLine())
                    .seat(reservation.getSeat())
                    .build();
        } else {
            throw new NoSuchElementException("예약 정보를 찾을 수 없습니다.");
        }
    }

    public PerformanceInfo getPerformanceInfoDetail(String name) {
        return PerformanceInfo.of(performanceRepository.findByName(name));
    }

    @Transactional
    public boolean reserve(ReserveInfo reserveInfo) {
        log.info("reserveInfo ID => {}", reserveInfo.getPerformanceId());
        Performance info = performanceRepository.findById(reserveInfo.getPerformanceId())
            .orElseThrow(EntityNotFoundException::new);
        String enableReserve = info.getIsReserve();
        if (enableReserve.equalsIgnoreCase("enable")) {
            // 1. 결제
            int price = info.getPrice();
            reserveInfo.setAmount(reserveInfo.getAmount() - price);
            // 2. 예매 진행
            reservationRepository.save(Reservation.of(reserveInfo));
            // 3. 좌석 정보 갱신
            PerformanceSeatInfo performanceSeatInfo =  performanceSeatInfoRepository.findByPerformanceIdAndRoundAndLineAndSeat(reserveInfo.getPerformanceId(),
                    reserveInfo.getRound(), reserveInfo.getLine(), reserveInfo.getSeat());
            performanceSeatInfo.setIsReserve(reserveInfo.getReservationStatus());

            // 4. 예약 가능 여부 갱신
            List<PerformanceSeatInfo> list = performanceSeatInfoRepository.findByPerformanceIdAndRoundAndIsReserve(reserveInfo.getPerformanceId(), reserveInfo.getRound(), "enable");

            if(list == null || list.isEmpty()) {
                info.setIsReserve("disable");
            }

            return true;

        } else {
            return false;
        }
    }

    public CancelInfo getCancelPerformance(String name) {

        Performance performance = performanceRepository.findFirstByIsReserveAndName("enable", name);
        PerformanceSeatInfo performanceSeatInfo = performanceSeatInfoRepository.findFirstByPerformanceIdAndRoundAndIsReserve(performance.getId(), performance.getRound(), "cancel");

        return CancelInfo.builder()
                .performanceId(performance.getId())
                .performanceName(performance.getName())
                .round(performance.getRound())
                .startDate(performance.getStart_date())
                .line(performanceSeatInfo.getLine())
                .seat(performanceSeatInfo.getSeat())
                .build();
    }
}
