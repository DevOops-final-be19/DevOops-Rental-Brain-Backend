package com.devoops.rentalbrain.customer.customerlist.query.dto;

import lombok.Data;

@Data
public class CustomerContractDTO {
    private Long customerId;
    private String customerCode;
    private String customerName;
    private String inCharge;
    private Integer segmentId;
    private String segmentName;
}
