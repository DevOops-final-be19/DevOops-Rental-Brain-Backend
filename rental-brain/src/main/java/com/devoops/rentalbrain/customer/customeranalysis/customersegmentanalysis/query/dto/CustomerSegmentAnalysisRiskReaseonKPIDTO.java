package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerSegmentAnalysisRiskReaseonKPIDTO {

    private String reasonCode;    // EXPIRING/LOW_SAT/OVERDUE/NO_RENEWAL
    private int count;
    private double ratio;            // (%)
}
