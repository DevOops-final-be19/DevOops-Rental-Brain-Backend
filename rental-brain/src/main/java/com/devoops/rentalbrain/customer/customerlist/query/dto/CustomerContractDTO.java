package com.devoops.rentalbrain.customer.customerlist.query.dto;

import lombok.Data;

@Data
public class CustomerContractDTO {
    private Long customerId;
    private String customerCode;
    private String customerName;
    private String inCharge;
    private Integer segmentId;
    private String segmentName;
    private String contractCode;  // 계약 번호
    private String startDate;     // 계약 시작일 (String 또는 LocalDate)
    private Integer period;       // 계약 기간
    private Integer monthlyPrice; // 월 납입금
}
