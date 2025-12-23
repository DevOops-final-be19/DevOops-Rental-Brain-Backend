package com.devoops.rentalbrain.business.contract.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOverdueCandidateDTO {
    private Long contractId;
    private Long customerId;

    private LocalDateTime oldestDueDate; // 가장 오래된 미납 회차 납부예정일
    private Integer overdueCount;        // 미납 회차 수 (계산 결과)
}
