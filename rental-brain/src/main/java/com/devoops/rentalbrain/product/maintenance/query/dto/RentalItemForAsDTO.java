package com.devoops.rentalbrain.product.maintenance.query.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RentalItemForAsDTO {

    private Long itemId;
    private String itemCode;
    private String itemName;
    private String itemStatus;
    private Integer repairCost;
    private LocalDateTime lastInspectDate;
}
