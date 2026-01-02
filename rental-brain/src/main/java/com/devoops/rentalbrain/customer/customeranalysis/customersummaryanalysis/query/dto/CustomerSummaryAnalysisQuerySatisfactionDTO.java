package com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CustomerSummaryAnalysisQuerySatisfactionDTO {

    // 5~1점 건수
    private long star5Count;
    private long star4Count;
    private long star3Count;
    private long star2Count;
    private long star1Count;

    // 전체 건수
    private long totalCount;

    // 5~1점 비율(0~100)
    private double star5Percent;
    private double star4Percent;
    private double star3Percent;
    private double star2Percent;
    private double star1Percent;
}
