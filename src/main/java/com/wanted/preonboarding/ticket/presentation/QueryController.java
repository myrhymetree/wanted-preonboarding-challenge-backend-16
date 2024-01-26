package com.wanted.preonboarding.ticket.presentation;

import com.wanted.preonboarding.core.domain.response.ResponseHandler;
import com.wanted.preonboarding.ticket.application.TicketSeller;
import com.wanted.preonboarding.ticket.domain.dto.CancelInfo;
import com.wanted.preonboarding.ticket.domain.dto.PerformanceInfo;
import com.wanted.preonboarding.ticket.domain.dto.ReserveInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("query")
@RequiredArgsConstructor
public class QueryController {
    private final TicketSeller ticketSeller;

    @GetMapping("/all/performance")
    public ResponseEntity<ResponseHandler<List<PerformanceInfo>>> getAllPerformanceInfoList() {
        System.out.println("getAllPerformanceInfoList");
        return ResponseEntity
            .ok()
            .body(ResponseHandler.<List<PerformanceInfo>>builder()
                .message("Success")
                .statusCode(HttpStatus.OK)
                .data(ticketSeller.getAllPerformanceInfoList())
                .build()
            );
    }

    @GetMapping("/detail")
    public ResponseHandler<ReserveInfo> findReservation(@RequestParam String name, @RequestParam String phone) {
        return new ResponseHandler(HttpStatus.OK, "예약자 정보 입니다.", ticketSeller.getReserveInfoDetail(name, phone));
    }

    @GetMapping("/cancel")
    public ResponseHandler<CancelInfo> findCancelPerformance(@RequestParam String name) {
        return new ResponseHandler(HttpStatus.OK, "예매가 취소된 공연 입니다.", ticketSeller.getCancelPerformance(name));
    }
}
