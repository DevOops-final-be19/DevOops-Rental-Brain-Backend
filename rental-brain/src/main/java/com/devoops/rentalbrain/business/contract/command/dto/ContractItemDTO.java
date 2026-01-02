package com.devoops.rentalbrain.business.contract.command.dto;

import lombok.Data;

@Data
public class ContractItemDTO {
    private String itemName;
    private int quantity;
}
