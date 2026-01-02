package com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerSummaryAnalysisQueryKPIDTO {

    private String currentMonth;     // YYYY-MM
    private String previousMonth;    // YYYY-MM

    private int tradeCustomerCount;        // 거래 고객 수 = 전체 - 잠재 - 블랙리스트

    private int totalCustomerCount;        // 전체 고객 수 (참고용)
    private int potentialCustomerCount;    // 잠재 고객 수 (segment_id=1)
    private int blacklistCustomerCount;    // 블랙리스트 고객 수 (segment_id=6)

    private int prevTradeCustomerCount;    // 전월 거래 고객 수 (전월대비 절대값 표시용)
    private int tradeCustomerMomDiff;      // 이번달-전월 (개사)
    private double tradeCustomerMomRate;   // 전월 대비 변화율(%)

    private long avgTradeAmount;     // 이번달 고객당 평균 월 과금액(원)
    private double avgTradeMomRate;  // 전월 대비 증감율(%)

    private double avgStar;          // 이번달 평균 별점
    private double avgStarMomDiff;   // 전월 대비 변화(점)

}
