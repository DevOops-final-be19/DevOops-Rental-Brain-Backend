package com.devoops.rentalbrain.business.contract.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContractSummaryDTO {
    // 전체 계약 수 (P, E, I, C)
    private long totalContracts;

    // 진행중
    private long progressContracts;

    // 만료임박
    private long imminentExpireContracts;

    // 이번 달 신규 계약
    private long thisMonthContracts;
}
