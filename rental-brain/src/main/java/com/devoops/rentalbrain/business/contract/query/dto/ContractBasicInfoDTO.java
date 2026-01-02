package com.devoops.rentalbrain.business.contract.query.dto;

import lombok.Data;

@Data
public class ContractBasicInfoDTO {

    private ContractOverviewDTO overview;
    private ContractProgressDTO progress;

    private int overdueCount;
    private int productCount;
}
