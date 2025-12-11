package com.devoops.rentalbrain.product.productlist.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ItemNameDTO {
    private String itemName;
    private String categoryName;
    private int monthlyPrice;
    private int stockAmount;
    private int rentalAmount;
    private int possibleAmount;
    private int repairAmount;
    private int overdueAmount;
    private int wholeSales;
    private int wholeRepairCost;
    private int utilizationRate;
}
