package com.devoops.rentalbrain.product.maintenance.query.service;

import com.devoops.rentalbrain.product.maintenance.query.dto.*;

import java.util.List;

public interface AfterServiceQueryService {

    List<AfterServiceDTO> findAll();

    AfterServiceSearchDTO findAll( int page, int size, String type, String status, String keyword, String summaryType, String sortField, String sortOrder);

    AfterServiceDetailDTO findById(Long id);

    AfterServiceSummaryDTO getSummary();

    int countNextWeek();
    List<NextWeekScheduleDTO> findNextWeekList();
}
