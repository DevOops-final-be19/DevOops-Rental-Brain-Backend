package com.devoops.rentalbrain.business.contract.query.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ContractItemDetailDTO {
    private Long itemId;
    private String itemCode;
    private String name;
    private LocalDate latelyInspectDate;
}
