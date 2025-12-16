package com.devoops.rentalbrain.business.contract.query.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AllContractDTO {
    private Long id;
    private String contract_code;
    private String cusName;
    private String in_charge;
    private String call_num;
    private String conName;
    private Date start_date;
    private Integer contract_period;
    private Integer monthly_payment;
    private String status;
}
