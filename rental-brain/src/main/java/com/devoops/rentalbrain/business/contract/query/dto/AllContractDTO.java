package com.devoops.rentalbrain.business.contract.query.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AllContractDTO {
    private Long id;
    private String contractCode;
    private String cusName;
    private String inCharge;
    private String callNum;
    private String conName;
    private Date startDate;
    private Integer contractPeriod;
    private Integer monthlyPayment;
    private String status;
}
