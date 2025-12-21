package com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class CustomerSupportAnalysisQuerySatisfactionKPIDTO {

    private double avgScore;        // 이번 달 평균(점)
    private double avgScoreMom;     // 전월 대비 변화(점)  예: +0.1, -0.2

    // 선택: 2.5점 이하 비중(%)  (지금은 feedback 기반으로 가능)
    private Double lowScoreRatio;       // 이번 달 (%)
    private Double lowScoreRatioMomP;   // 전월 대비 (%p)
}
