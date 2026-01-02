package com.devoops.rentalbrain.customer.overdue.query.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ItemOverdueListDTO {

    private Long overdueId;
    private String itemOverdueCode;

    private String customerName;
    private String inCharge;
    private String callNum;

    private String contractCode;

    private Integer count;            // 연체 수량
    private LocalDateTime dueDate;    // 반납 예정일
    private Integer overduePeriod;    // 연체 기간
    private String status;            // P / C
}
