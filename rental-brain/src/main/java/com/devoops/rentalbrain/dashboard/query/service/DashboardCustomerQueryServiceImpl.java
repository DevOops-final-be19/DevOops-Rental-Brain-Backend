package com.devoops.rentalbrain.dashboard.query.service;

import com.devoops.rentalbrain.dashboard.query.dto.DashboardKpiResponseDTO;
import com.devoops.rentalbrain.dashboard.query.dto.QuarterlyCustomerTrendResponseDTO;
import com.devoops.rentalbrain.dashboard.query.mapper.DashboardCustomerQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class DashboardCustomerQueryServiceImpl implements DashboardCustomerQueryService {

    private final DashboardCustomerQueryMapper mapper;

    // 분기별 고객 수
    @Override
    public QuarterlyCustomerTrendResponseDTO getQuarterlyTrend(Integer year) {
        int y = (year != null) ? year : LocalDate.now().getYear();
        return QuarterlyCustomerTrendResponseDTO.builder()
                .year(y)
                .series(mapper.selectQuarterlyCustomerTrend(y))
                .build();
    }

    // kpi
    @Override
    public DashboardKpiResponseDTO getDashboardKpi(String month) {

        YearMonth ym = (month == null || month.isBlank())
                ? YearMonth.now()
                : YearMonth.parse(month);

        YearMonth prevYm = ym.minusMonths(1);

        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.atEndOfMonth().atTime(23, 59, 59);

        LocalDateTime prevStart = prevYm.atDay(1).atStartOfDay();
        LocalDateTime prevEnd = prevYm.atEndOfMonth().atTime(23, 59, 59);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime until = now.plusDays(60);

        int expiring = mapper.countExpiringContractsNext60(now, until);
        int overdue = mapper.countPayOverdueInProgress();
        int waiting = mapper.countWaitingInquiries();

        long revenue = mapper.sumRevenueBetween(start, end);
        long prevRevenue = mapper.sumRevenueBetween(prevStart, prevEnd);

        double momRate = calcMomRate(revenue, prevRevenue);

        return DashboardKpiResponseDTO.builder()
                .month(ym.toString())
                .expiringContractCount(expiring)
                .payOverdueCount(overdue)
                .waitingInquiryCount(waiting)
                .mtdRevenue(revenue)
                .momRevenueRate(round1(momRate))
                .build();
    }

    private double calcMomRate(long current, long previous) {
        if (previous <= 0) {
            return (current > 0) ? 100.0 : 0.0;
        }
        return ((double) (current - previous) / previous) * 100.0;
    }

    private double round1(double v) {
        return Math.round(v * 10.0) / 10.0;
    }
}
