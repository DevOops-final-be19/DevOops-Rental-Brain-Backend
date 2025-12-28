package com.devoops.rentalbrain.customer.overdue.query.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ItemOverdueDetailDTO {

    private Long overdueId;
    private String itemOverdueCode;

    private String customerName;
    private String inCharge;
    private String callNum;

    private Long contractId;
    private String contractCode;

    private Integer count;
    private LocalDateTime dueDate;
    private Integer overduePeriod;
    private String status;
}
