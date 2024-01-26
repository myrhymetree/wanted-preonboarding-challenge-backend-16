package com.wanted.preonboarding.ticket.domain.entity;

import com.wanted.preonboarding.ticket.domain.dto.ReserveInfo;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PerformanceSeatInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "performance_id")
    private UUID performanceId;

    @Column(name = "round")
    private int round;

    @Column(name = "gate")
    private int gate;

    @Column(name = "line")
    private char line;

    @Column(name = "seat")
    private int seat;

    @Column(name = "is_reserve")
    private String isReserve;
}
