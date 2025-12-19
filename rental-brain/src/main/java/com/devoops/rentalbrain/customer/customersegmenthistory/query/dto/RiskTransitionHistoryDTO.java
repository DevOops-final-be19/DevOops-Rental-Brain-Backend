package com.devoops.rentalbrain.customer.customersegmenthistory.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RiskTransitionHistoryDTO {

    private Long id;
    private Long customerId;

    // customer join
    private String customerName;

    private Long fromSegmentId;
    private String fromSegmentName;

    private Long toSegmentId;
    private String toSegmentName;

    private String reasonCode;
    private String reason;

    private String triggerType;
    private String referenceType;
    private Long referenceId;

    private LocalDateTime changedAt;
}
