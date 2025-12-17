package com.devoops.rentalbrain.product.productlist.command.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ItemDTO {
    private String name;
    private int monthlyPrice;
    private String categoryName;
}
