package com.devoops.rentalbrain.customer.overdue.query.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ItemOverdueListDTO {
    private Long id;
    private String itemOverdueCode;
    private String customerName;
    private String contractName;
    private Integer count;
    private LocalDateTime dueDate;
    private Integer overduePeriod;
    private String status;
}
