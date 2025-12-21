package com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChurnKpiCardResponseDTO {

    private String snapshotMonth;          // 기준월(YYYY-MM)
    private String prevMonth;              // 전월(YYYY-MM)

    private int totalCustomerCount;        // 전체 customer 수

    // 이번달
    private int curRiskCustomerCount;      // 이번달 위험 고객 수
    private double curRiskRate;            // 이번달 위험률(%)

    // 전월
    private int prevRiskCustomerCount;     // 전월 위험 고객 수
    private double prevRiskRate;           // 전월 위험률(%)

    // 전월 대비 증감(%p)
    private double momDiffRate;            // curRiskRate - prevRiskRate

    // 유지(이번달)
    private int curRetainedCustomerCount;  // 이번달 유지 고객 수 = total - curRisk
    private double curRetentionRate;       // 이번달 유지율(%) = 100 - curRiskRate
}