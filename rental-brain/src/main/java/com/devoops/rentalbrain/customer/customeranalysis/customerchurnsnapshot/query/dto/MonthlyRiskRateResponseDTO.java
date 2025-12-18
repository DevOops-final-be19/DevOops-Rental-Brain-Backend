package com.devoops.rentalbrain.customer.customeranalysis.customerchurnsnapshot.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MonthlyRiskRateResponseDTO {

    private String snapshotMonth; // YYYY-MM
    private int riskCustomerCount; // 해당 월 위험 고객 수
    private double riskRate;       // 해당 월 위험률(%)
}