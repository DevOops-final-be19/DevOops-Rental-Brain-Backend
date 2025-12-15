package com.devoops.rentalbrain.business.contract.command.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContractCreateDTO {
    private String contractName;
    private LocalDateTime startDate;
    private Integer contractPeriod;
    private Integer monthlyPayment;
    private Long totalAmount;
    private String payMethod;
    private String specialContent;
    private Long cumId;
    private Long memId;
    private Long leaderId;
    private Long ceoId;
}
