package com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSupportAnalysisMonthlyTrendResponseDTO {

    private int year;
    private List<CustomerSupportAnalysisMonthlyTrendDTO> monthly; // 1~12월
}