package com.devoops.rentalbrain.product.productlist.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ItemKpiDTO {
    private int wholeCount;
    private int rentalCount;
    private int repairCount;
    private Integer availableCount;
    private Integer overdueCount;
}
