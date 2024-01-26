package com.wanted.preonboarding.ticket.domain.dto.response;

import lombok.Builder;

@Builder
public class ResponseReservation {
    CompletedReservationInfo completedReservationInfo;
    CustomerInfo customerInfo;
}
