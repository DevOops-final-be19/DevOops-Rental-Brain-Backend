package com.devoops.rentalbrain.product.maintenance.query.controller;

import com.devoops.rentalbrain.product.maintenance.query.dto.AfterServiceDetailResponse;
import com.devoops.rentalbrain.product.maintenance.query.dto.AfterServiceResponse;
import com.devoops.rentalbrain.product.maintenance.query.dto.AfterServiceSummaryResponse;
import com.devoops.rentalbrain.product.maintenance.query.dto.NextWeekScheduleResponse;
import com.devoops.rentalbrain.product.maintenance.query.service.AfterServiceQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/as")
public class AfterServiceQueryController {

    private final AfterServiceQueryService afterServiceQueryService;

    @GetMapping
    public List<AfterServiceResponse> findAll() {
        return afterServiceQueryService.findAll();
    }

    @GetMapping("/{asId}")
    public AfterServiceDetailResponse findDetail(@PathVariable Long asId) {
        return afterServiceQueryService.findById(asId);
    }

    @GetMapping("/summary")
    public AfterServiceSummaryResponse summary() {
        return afterServiceQueryService.getSummary();
    }

    @GetMapping("/upcoming")
    public List<NextWeekScheduleResponse> nextWeekList() {
        return afterServiceQueryService.findNextWeekList();
    }

    @GetMapping("/upcoming/count")
    public int nextWeekCount() {
        return afterServiceQueryService.countNextWeek();
    }
}
