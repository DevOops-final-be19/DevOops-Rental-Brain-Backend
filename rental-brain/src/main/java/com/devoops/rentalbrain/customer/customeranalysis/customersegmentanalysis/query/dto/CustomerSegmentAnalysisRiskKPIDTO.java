package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerSegmentAnalysisRiskKPIDTO {

    private String currentMonth;    // 현재 기준월
    private String previousMonth;   // 전월

    private int totalCustomerCount;  // 전체 고객수
    private int currentRiskCustomerCount; // 현재 이탈 위험 고객수

    private double currentRiskRate;     // 현재 이탈 위험률
    private double momDiffRate;         // 전월 대비 이탈 위험률

}
