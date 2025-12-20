package com.devoops.rentalbrain.customer.customersegmenthistory.query.service;

import com.devoops.rentalbrain.customer.customersegmenthistory.query.dto.RiskTransitionCountDTO;
import com.devoops.rentalbrain.customer.customersegmenthistory.query.dto.RiskTransitionHistoryDTO;

import java.util.List;

public interface RiskTransitionQueryService {
    List<RiskTransitionHistoryDTO> getChurnRiskTransitions(Long customerId);

    List<RiskTransitionHistoryDTO> getBlacklistTransitions(Long customerId);

    RiskTransitionCountDTO getRiskTransitionCount(Long customerId); // ✅ 추가 (요약)

}
