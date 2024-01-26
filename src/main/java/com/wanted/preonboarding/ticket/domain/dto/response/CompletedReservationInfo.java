package com.wanted.preonboarding.ticket.domain.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public class CompletedReservationInfo {
    private UUID performanceId;     // 공연ID
    private String performanceName; // 공연명
    private int round;              // 회차
    private int seat;               // 좌석번호
}
