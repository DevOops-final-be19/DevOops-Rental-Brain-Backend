package com.devoops.rentalbrain.product.maintenance.query.service;

import com.devoops.rentalbrain.product.maintenance.query.dto.AfterServiceDetailResponse;
import com.devoops.rentalbrain.product.maintenance.query.dto.AfterServiceResponse;
import com.devoops.rentalbrain.product.maintenance.query.dto.AfterServiceSummaryResponse;
import com.devoops.rentalbrain.product.maintenance.query.dto.NextWeekScheduleResponse;

import java.util.List;

public interface AfterServiceQueryService {

    List<AfterServiceResponse> findAll();
    AfterServiceDetailResponse findById(Long id);

    AfterServiceSummaryResponse getSummary();

    int countNextWeek();
    List<NextWeekScheduleResponse> findNextWeekList();
}
