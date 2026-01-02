package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MonthlyRiskRateResponseDTO {

    // 이탈 위험률 차트
    private String snapshotMonth; // YYYY-MM (월말 기준)
    private int riskCustomerCount; // 해당 월 위험 고객 수
    private double riskRate;       // 해당 월 위험률(%)
}