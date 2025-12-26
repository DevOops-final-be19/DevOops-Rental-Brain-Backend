package com.devoops.rentalbrain.customer.overdue.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemOverdueCandidateDTO {
    private Long contractId;
    private Long customerId;

    /** 계약 만료일 (= 제품 연체 기준일) */
    private LocalDateTime dueDate;

    /** 계약에 묶인 제품 수 */
    private Integer itemCount;

    /** 오늘 기준 연체 일수 */
    private Integer overduePeriod;
}
