package com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerSummaryAnalysisQuerySatisfactionRowDTO {

    private long star5Count;
    private long star4Count;
    private long star3Count;
    private long star2Count;
    private long star1Count;
    private long totalCount;
}
