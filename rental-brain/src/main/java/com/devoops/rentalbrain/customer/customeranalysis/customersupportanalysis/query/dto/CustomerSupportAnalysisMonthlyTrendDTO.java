package com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSupportAnalysisMonthlyTrendDTO {
    private int month;          // 1~12
    private long quoteCount;    // 견적상담
    private long SupportCount;  // 문의(customer_support)
    private long feedbackCount; // 만족도(피드백)
    private long surveyCount;   // 설문조사
}
