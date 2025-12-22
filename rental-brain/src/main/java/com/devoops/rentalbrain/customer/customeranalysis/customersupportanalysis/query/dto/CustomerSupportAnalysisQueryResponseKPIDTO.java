package com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.dto;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class CustomerSupportAnalysisQueryResponseKPIDTO {


    private String targetMonth;   // "2024-12"
    private String previousMonth; // "2024-11"

    // KPI 1
    private CustomerSupportAnalysisQueryKPICardDTO totalResponseCard;

    // KPI 2
    private CustomerSupportAnalysisQueryEfficiencyKPIDTO efficiency;

    // KPI 3
    private CustomerSupportAnalysisQuerySatisfactionKPIDTO satisfaction;

}
