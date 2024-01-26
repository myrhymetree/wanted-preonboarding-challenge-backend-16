package com.wanted.preonboarding.ticket.domain.dto;

import lombok.Builder;

import java.util.Date;
import java.util.UUID;

@Builder
public class CancelInfo {
    private UUID performanceId;
    private String performanceName;
    private Date startDate;
    private int round;
    private char line;
    private int seat;
}
