package com.devoops.rentalbrain.customer.customeranalysis.customerchurnsnapshot.query.service;

import com.devoops.rentalbrain.customer.customeranalysis.customerchurnsnapshot.query.dto.ChurnKpiCardResponseDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customerchurnsnapshot.query.dto.MonthlyRiskRateResponseDTO;

import java.util.List;

public interface CustomerChurnSnapshotQueryService {
    ChurnKpiCardResponseDTO getKpiCard(String month);

    List<MonthlyRiskRateResponseDTO> getMonthlyRiskRate(String fromMonth,
                                                        String toMonth);
}
