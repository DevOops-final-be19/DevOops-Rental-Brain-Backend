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

    // KPI
    @Override
    public DashboardKpiResponseDTO getDashboardKpi(String month) {

        YearMonth ym = (month == null || month.isBlank())
                ? YearMonth.now()
                : YearMonth.parse(month);

        YearMonth prevYm = ym.minusMonths(1);

        // ✅ [start, endExclusive) 방식 (끝은 다음달 1일 00:00)
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime endExclusive = ym.plusMonths(1).atDay(1).atStartOfDay();

        LocalDateTime prevStart = prevYm.atDay(1).atStartOfDay();
        LocalDateTime prevEndExclusive = prevYm.plusMonths(1).atDay(1).atStartOfDay();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime until = now.plusDays(60);

        int expiring = mapper.countExpiringContractsNext60(now, until);
        int overdue = mapper.countPayOverdueInProgress();
        int waiting = mapper.countWaitingInquiries();

        // ✅ LocalDateTime 그대로 전달 (문자열 포맷 제거)
        long revenue = mapper.sumMonthlyRevenueBetween(start, endExclusive);
        long prevRevenue = mapper.sumMonthlyRevenueBetween(prevStart, prevEndExclusive);

        long diff = revenue - prevRevenue;
        double momRate = calcMomRate(revenue, prevRevenue);

        return DashboardKpiResponseDTO.builder()
                .month(ym.toString())
                .expiringContractCount(expiring)
                .payOverdueCount(overdue)
                .waitingInquiryCount(waiting)

                .mtdRevenue(revenue)
                .prevMtdRevenue(prevRevenue)
                .momRevenueDiff(diff)
                .momRevenueRate(round1(momRate))
                .build();
    }

    private double calcMomRate(long current, long previous) {
        if (previous <= 0) {
            // 전월 0이면 "신규" 성격 → 기존 유지
            return (current > 0) ? 100.0 : 0.0;
        }
        return ((double) (current - previous) / previous) * 100.0;
    }

    private double round1(double v) {
        return Math.round(v * 10.0) / 10.0;
    }
}
