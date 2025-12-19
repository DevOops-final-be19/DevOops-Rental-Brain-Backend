package com.devoops.rentalbrain.business.contract.query.dto;

import lombok.Data;

import java.util.List;

@Data
public class ContractItemInfoDTO {
    private List<ContractItemSummaryDTO> contractItemSummary;
    private List<ContractItemDetailDTO> contractItemDetail;
}
