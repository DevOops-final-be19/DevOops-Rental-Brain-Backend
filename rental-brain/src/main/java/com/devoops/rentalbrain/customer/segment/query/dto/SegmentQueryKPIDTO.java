package com.devoops.rentalbrain.customer.segment.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SegmentQueryKPIDTO {

    // 이탈 위험 고객 비중
    // {이탈 위험 고객군(갯수) + 피드백 평점 2.5점 이하 고객} / 전체 고객군(갯수) X 100 (%)
    private double riskCustomerRate;     // %
    private int riskCustomerCount;       // N개사
    private int totalCustomerCount;      // 전체 고객수

    // 전월 대비 변화 (riskCustomerRate 또는 만족도 기반 proxy)
    private double momChangeRate;        // %p

    // 가장 위험한 세그먼트 (세그먼트 이탈 위험 가능성)
    //이번달 저평점(피드백 평점2.5점 이하) 고객 수 / 해당 세그먼트 전체 고객 수) * 100 (%)
    private String mostRiskySegmentName;
    private double mostRiskySegmentRate; // %

    // 최저 만족도 세그먼트
    private String lowestSatisfactionSegmentName;
    private double lowestSatisfactionAvgStar; // avg
}
