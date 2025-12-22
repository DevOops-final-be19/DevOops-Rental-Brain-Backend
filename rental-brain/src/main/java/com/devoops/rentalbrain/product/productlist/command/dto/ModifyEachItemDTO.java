package com.devoops.rentalbrain.product.productlist.command.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ModifyEachItemDTO {
    private String status;
    private LocalDateTime lastInspectDate;
    private Integer sales;
    private Integer repairCost;
}
