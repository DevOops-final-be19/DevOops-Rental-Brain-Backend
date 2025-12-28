package com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomerSummaryAnalysisQuerySatisfactionCustomerDTO {
    private Integer star;              // 최근 별점
    private Long customerId;
    private String customerCode;
    private String customerName;
    private String segmentName;
    private LocalDateTime createDate;  // 최근 피드백 일시
}
