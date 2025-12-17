package com.devoops.rentalbrain.customer.segment.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SegmentQueryKPIDTO {

    // 이탈 위험 고객 비중
    private double riskCustomerRate;     // %
    private int riskCustomerCount;       // 이탈 위험 고객 고객사 수
    private int totalCustomerCount;      // 전체 고객수

    // 가장 위험한 세그먼트 (이번달 세그먼트 평균 평점 최저)
    private String mostRiskySegmentName;
    private double mostRiskySegmentRate; // 평균 평점(낮을수록 위험)

    // 분포 KPI: 2.5 이하 세그먼트 개수
    private int lowSatisfactionSegmentCount; // avg <= 2.5 세그먼트 수
    private int totalSegmentCount;           // 전체 세그먼트 수(고객 포함 기준)
}
