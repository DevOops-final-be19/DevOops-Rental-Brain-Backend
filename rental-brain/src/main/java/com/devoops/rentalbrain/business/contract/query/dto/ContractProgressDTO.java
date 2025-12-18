package com.devoops.rentalbrain.business.contract.query.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ContractProgressDTO {
    private Long contractId;
    private LocalDate startDate;
    private Integer contractPeriod;
    private Integer progressRate; // %
}
