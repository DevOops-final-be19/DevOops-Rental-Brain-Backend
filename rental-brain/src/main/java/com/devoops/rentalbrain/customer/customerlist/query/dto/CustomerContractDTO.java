package com.devoops.rentalbrain.customer.customerlist.query.dto;

import lombok.Data;

@Data
public class CustomerContractDTO {
    Long customerId;
    String customerCode;
    String customerName;
    String inCharge;
    String segmentName;
}
