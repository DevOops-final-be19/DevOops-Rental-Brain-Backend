package com.devoops.rentalbrain.product.maintenance.query.service;

import com.devoops.rentalbrain.product.maintenance.query.dto.*;
import com.devoops.rentalbrain.product.maintenance.query.mapper.AfterServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AfterServiceQueryServiceImpl implements AfterServiceQueryService {

    private final AfterServiceMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public List<AfterServiceDTO> findAll() {
        return mapper.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public AfterServiceSearchDTO findAll(int page, int size, String type, String status, String keyword, String summaryType, String sortField, String sortOrder) {

        int offset = (page - 1) * size;

        List<AfterServiceDTO> list =
                mapper.findAllWithPaging(offset, size, type, status, keyword, summaryType, sortField, sortOrder);

        long totalCount =
                mapper.countAll(type, status, keyword, summaryType);

        int totalPages = (int) Math.ceil((double) totalCount / size);

        return new AfterServiceSearchDTO( list, page, size, totalCount, totalPages );
    }

    @Transactional(readOnly = true)
    @Override
    public AfterServiceDetailDTO findById(Long id) {
        return mapper.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public AfterServiceSummaryDTO getSummary() {
        return new AfterServiceSummaryDTO(
                mapper.countThisMonthSchedule(),
                mapper.countImminent72h(),
                mapper.countThisMonthCompleted(),
                mapper.countTodayInProgress()
        );
    }

    @Transactional(readOnly = true)
    @Override
    public int countNextWeek() {
        return mapper.countNextWeekSchedule();
    }

    @Transactional(readOnly = true)
    @Override
    public List<NextWeekScheduleDTO> findNextWeekList() {
        return mapper.findNextWeekScheduleList();
    }
}
