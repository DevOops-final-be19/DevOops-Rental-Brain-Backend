package com.devoops.rentalbrain.customer.overdue.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OverdueItemDTO {

    private String itemCode;
    private String itemName;
    private String itemStatus;
}
