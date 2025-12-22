package com.devoops.rentalbrain.customer.overdue.command.dto;

import lombok.Getter;

@Getter
public class ItemOverdueCommandDTO {
    private Integer count;
    private String status; // C 처리
}
