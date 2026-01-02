package com.devoops.rentalbrain.product.productlist.query.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EachItemDTO {
    private Long id;
    private String itemCode;
    private String status;
    private Integer sales;
    private Integer repairCost;
    private String firmName;
    private LocalDateTime startDate;
    private LocalDateTime lastInspectDate;
}
