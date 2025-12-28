package com.devoops.rentalbrain.customer.overdue.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PayOverdueDetailDTO {

    private Long overdueId;
    private String payOverdueCode;

    private String customerName;
    private String inCharge;
    private String callNum;

    private String contractCode;
    private Integer monthlyPayment;

    private LocalDateTime dueDate;
    private Integer overduePeriod;
    private String status;
    private LocalDateTime paidDate;
}
