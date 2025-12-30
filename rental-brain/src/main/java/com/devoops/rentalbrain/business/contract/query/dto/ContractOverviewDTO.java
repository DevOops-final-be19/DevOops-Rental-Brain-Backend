package com.devoops.rentalbrain.business.contract.query.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ContractOverviewDTO {
    /* 계약 정보 */
    private Long contractId;
    private String contractCode;
    private String contractName;
    private LocalDate startDate;
    private Integer contractPeriod;
    private Long monthlyPayment;
    private Long totalAmount;
    private String payMethod;
    private String specialContent;
    private String contractStatus;

    /* 고객 정보 */
    private Long customerId;
    private String customerCode;
    private String customerName;
    private String inCharge;
    private String callNum;
}
