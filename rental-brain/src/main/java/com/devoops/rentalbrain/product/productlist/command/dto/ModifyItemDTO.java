package com.devoops.rentalbrain.product.productlist.command.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ModifyItemDTO {
    private String name;
    private Integer monthlyPrice;
    private String categoryName;
}
