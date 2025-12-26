package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CustomerSegmentAnalysisRiskReasonCustomerDTO {
    private Long customerId;
    private String customerCode;
    private String customerName;
    private String inCharge;
    private String dept;
    private String callNum;
    private String isDeleted;

    // 이 사유로 전환된 시점(리스트에서 꽤 유용함)
    private LocalDateTime changedAt;
}
