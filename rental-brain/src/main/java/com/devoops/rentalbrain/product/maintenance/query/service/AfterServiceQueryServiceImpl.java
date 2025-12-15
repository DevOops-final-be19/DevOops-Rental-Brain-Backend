package com.devoops.rentalbrain.product.maintenance.query.service;

import com.devoops.rentalbrain.product.maintenance.query.dto.AfterServiceDetailResponse;
import com.devoops.rentalbrain.product.maintenance.query.dto.AfterServiceResponse;
import com.devoops.rentalbrain.product.maintenance.query.dto.AfterServiceSummaryResponse;
import com.devoops.rentalbrain.product.maintenance.query.dto.NextWeekScheduleResponse;
import com.devoops.rentalbrain.product.maintenance.query.mapper.AfterServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AfterServiceQueryServiceImpl implements AfterServiceQueryService {

    private final AfterServiceMapper mapper;

    @Override
    public List<AfterServiceResponse> findAll() {
        return mapper.findAll();
    }

    @Override
    public AfterServiceDetailResponse findById(Long id) {
        return mapper.findById(id);
    }

    @Override
    public AfterServiceSummaryResponse getSummary() {
        return new AfterServiceSummaryResponse(
                mapper.countThisMonthSchedule(),
                mapper.countImminent72h(),
                mapper.countThisMonthCompleted(),
                mapper.countTodayInProgress()
        );
    }

    @Override
    public int countNextWeek() {
        return mapper.countNextWeekSchedule();
    }

    @Override
    public List<NextWeekScheduleResponse> findNextWeekList() {
        return mapper.findNextWeekScheduleList();
    }
}
