package com.devoops.rentalbrain.product.productlist.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EachItemKpiDTO {
    private Integer totalCount;
    private Integer rentCount;
    private Integer availableCount;
    private Integer repairCount;
    private Integer overdueCount;
}
