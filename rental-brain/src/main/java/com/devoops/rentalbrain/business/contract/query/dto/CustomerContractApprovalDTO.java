package com.devoops.rentalbrain.business.contract.query.dto;

import lombok.Data;

@Data
public class CustomerContractApprovalDTO {
    private Long customerId;
    private String customerCode;
    private String customerName;
    private String inCharge;
    private Integer segmentId;
    private String segmentName;
}
