package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.service;

import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.*;
import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.mapper.CustomerSegmentAnalysisQueryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CustomerSegmentAnalysisQueryServiceImpl implements CustomerSegmentAnalysisQueryService {

    private final CustomerSegmentAnalysisQueryMapper customerSegmentAnalysisQueryMapper;

    private static final int RISK_SEGMENT_ID = 4;

    // MariaDB DATETIME 비교 안전 포맷
    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public CustomerSegmentAnalysisQueryServiceImpl(CustomerSegmentAnalysisQueryMapper customerSegmentAnalysisQueryMapper) {
        this.customerSegmentAnalysisQueryMapper = customerSegmentAnalysisQueryMapper;
    }

    // KPI 1) 이탈 위험률 카드 (월말 기준)
    @Override
    public ChurnKpiCardResponseDTO getRiskKpi(String month) {

        if (month == null || month.isBlank()) {
            throw new IllegalArgumentException("month 파라미터는 필수입니다. 예: 2026-01");
        }

        YearMonth ym = YearMonth.parse(month);
        YearMonth prevYm = ym.minusMonths(1);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String monthEndExclusive =
                ym.plusMonths(1).atDay(1).atStartOfDay().format(fmt);

        String prevEndExclusive =
                ym.atDay(1).atStartOfDay().format(fmt);

        //  월말 기준 전체 고객 수
        int total = customerSegmentAnalysisQueryMapper
                .countTotalCustomersAtMonth(monthEndExclusive);

        int prevTotal = customerSegmentAnalysisQueryMapper
                .countTotalCustomersAtMonth(prevEndExclusive);

        //  월말 기준 위험 고객 수 (segment_id = 4)
        int curRisk = customerSegmentAnalysisQueryMapper
                .countCustomersBySegmentAtMonth(RISK_SEGMENT_ID, monthEndExclusive);

        int prevRisk = customerSegmentAnalysisQueryMapper
                .countCustomersBySegmentAtMonth(RISK_SEGMENT_ID, prevEndExclusive);

        double curRiskRate = rate(curRisk, total);
        double prevRiskRate = rate(prevRisk, prevTotal);

        return ChurnKpiCardResponseDTO.builder()
                .snapshotMonth(month)
                .prevMonth(prevYm.toString())
                .totalCustomerCount(total)
                .curRiskCustomerCount(curRisk)
                .curRiskRate(curRiskRate)
                .prevRiskCustomerCount(prevRisk)
                .prevRiskRate(prevRiskRate)
                .momDiffRate(round1(curRiskRate - prevRiskRate))
                .build();
    }

    // 차트) 월별 이탈 위험률 (월말 기준)
    @Override
    public List<MonthlyRiskRateResponseDTO> getMonthlyRiskRate(String fromMonth, String toMonth) {

        if (fromMonth == null || fromMonth.isBlank() || toMonth == null || toMonth.isBlank()) {
            throw new IllegalArgumentException("from/to 파라미터는 필수입니다. 예: from=2025-01&to=2025-06");
        }

        YearMonth from = YearMonth.parse(fromMonth);
        YearMonth to = YearMonth.parse(toMonth);

        if (from.isAfter(to)) {
            throw new IllegalArgumentException("기간이 올바르지 않습니다. from은 to보다 클 수 없습니다.");
        }

        List<MonthlyRiskRateResponseDTO> result = new ArrayList<>();

        YearMonth cur = from;
        while (!cur.isAfter(to)) {
            String m = cur.toString();
            String endExclusive = monthEndExclusive(cur);

            int total = customerSegmentAnalysisQueryMapper.countTotalCustomersAtMonth(endExclusive);
            int risk = customerSegmentAnalysisQueryMapper.countCustomersBySegmentAtMonth(RISK_SEGMENT_ID, endExclusive);

            result.add(MonthlyRiskRateResponseDTO.builder()
                    .snapshotMonth(m)                 // 필드명 유지(프론트 깨짐 방지) / 의미는 "해당 월(월말 기준)"
                    .riskCustomerCount(risk)
                    .riskRate(rate(risk, total))
                    .build());

            cur = cur.plusMonths(1);
        }

        return result;
    }

    // KPI 2) 이탈 위험 사유 분포 (월 기준)
    @Override
    public List<CustomerSegmentAnalysisRiskReaseonKPIDTO> getRiskReasonKpi(String month) {

        if (month == null || month.isBlank()) {
            throw new IllegalArgumentException("month 파라미터는 필수입니다. 예: 2025-02");
        }

        YearMonth ym = YearMonth.parse(month);

        String from = monthStart(ym);
        String to = monthStart(ym.plusMonths(1));

        List<Map<String, Object>> rows =
                customerSegmentAnalysisQueryMapper.countRiskReasonsByMonth(RISK_SEGMENT_ID, from, to);

        List<String> fixedCodes = List.of("EXPIRING", "LOW_SAT", "OVERDUE", "NO_RENEWAL");

        java.util.Map<String, Integer> countMap = new java.util.LinkedHashMap<>();
        for (String code : fixedCodes) countMap.put(code, 0);

        for (Map<String, Object> r : rows) {
            String code = (String) r.get("reasonCode");
            int cnt = ((Number) r.get("cnt")).intValue();
            if (countMap.containsKey(code)) {
                countMap.put(code, cnt);
            }
        }

        int total = countMap.values().stream().mapToInt(Integer::intValue).sum();

        List<CustomerSegmentAnalysisRiskReaseonKPIDTO> result = new ArrayList<>();
        for (String code : fixedCodes) {
            int cnt = countMap.get(code);
            double ratio = (total == 0) ? 0.0 : round1((double) cnt / total * 100.0);

            result.add(CustomerSegmentAnalysisRiskReaseonKPIDTO.builder()
                    .reasonCode(code)
                    .count(cnt)
                    .ratio(ratio)
                    .build());
        }

        return result;
    }

    // KPI 2) 사유별 고객 리스트 (월 기준)
    @Override
    public CustomerSegmentAnalysisRiskReasonCustomersListDTO getRiskReasonCustomers(String month, String reasonCode) {

        if (month == null || month.isBlank()) {
            throw new IllegalArgumentException("month 파라미터는 필수입니다. 예: 2025-02");
        }
        if (reasonCode == null || reasonCode.isBlank()) {
            throw new IllegalArgumentException("reasonCode 파라미터는 필수입니다.");
        }

        List<String> fixedCodes = List.of("EXPIRING", "LOW_SAT", "OVERDUE", "NO_RENEWAL");
        if (!fixedCodes.contains(reasonCode)) {
            throw new IllegalArgumentException("허용되지 않는 reasonCode 입니다: " + reasonCode);
        }

        YearMonth ym = YearMonth.parse(month);

        String from = monthStart(ym);
        String to = monthStart(ym.plusMonths(1));

        var list = customerSegmentAnalysisQueryMapper.findRiskReasonCustomersByMonth(
                RISK_SEGMENT_ID, reasonCode, from, to
        );

        return CustomerSegmentAnalysisRiskReasonCustomersListDTO.builder()
                .month(month)
                .reasonCode(reasonCode)
                .totalCount(list == null ? 0 : list.size())
                .customers(list)
                .build();
    }

    @Override
    public List<CustomerSegmentTradeChartDTO> getSegmentTradeChart(String month) {

        if (month == null || month.isBlank()) {
            throw new IllegalArgumentException("month 파라미터는 필수 입니다. 예: YYYY-MM");
        }

        YearMonth yearMonth = YearMonth.parse(month);

        String from = monthStart(yearMonth);
        String to = monthStart(yearMonth.plusMonths(1));

        return customerSegmentAnalysisQueryMapper.getSegmentTradeChart(from, to);
    }

    @Override
    public CustomerSegmentDetailCardDTO getSegmentDetailCard(long segmentId) {

        CustomerSegmentDetailCardDTO card =
                customerSegmentAnalysisQueryMapper.getSegmentDetailBase(segmentId);

        if (card == null) {
            return CustomerSegmentDetailCardDTO.builder()
                    .segmentId(segmentId)
                    .segmentName("-")
                    .customerCount(0)
                    .totalTradeAmount(0)
                    .avgTradeAmount(0)
                    .avgSatisfaction(0)
                    .topItemName("-")
                    .topSupport("-")
                    .topFeedback("-")
                    .build();
        }

        Double avgStar = customerSegmentAnalysisQueryMapper.getSegmentAvgStar(segmentId);
        String topItem = customerSegmentAnalysisQueryMapper.getTopItemName(segmentId);
        String topSupport = customerSegmentAnalysisQueryMapper.getTopSupport(segmentId);
        String topFeedback = customerSegmentAnalysisQueryMapper.getTopFeedback(segmentId);

        card.setAvgSatisfaction(avgStar == null ? 0.0 : avgStar);
        card.setTopItemName((topItem == null || topItem.isBlank()) ? "-" : topItem);
        card.setTopSupport((topSupport == null || topSupport.isBlank()) ? "-" : topSupport);
        card.setTopFeedback((topFeedback == null || topFeedback.isBlank()) ? "-" : topFeedback);

        return card;
    }

    @Override
    public CustomerSegmentRiskCustomerPageDTO getRiskCustomersByMonth(String month, int page, int size) {

        if (month == null || month.isBlank()) {
            throw new IllegalArgumentException("month 파라미터는 필수입니다. 예: 2026-01");
        }

        if (page < 1) page = 1;
        if (size < 1) size = 20;

        YearMonth ym = YearMonth.parse(month);
        String endExclusive = monthEndExclusive(ym);

        int offset = (page - 1) * size;

        int total = customerSegmentAnalysisQueryMapper.countRiskCustomersAtMonth(endExclusive);
        List<CustomerSegmentRiskCustomerDTO> list =
                customerSegmentAnalysisQueryMapper.findRiskCustomersAtMonth(endExclusive, size, offset);

        return CustomerSegmentRiskCustomerPageDTO.builder()
                .month(month)
                .totalCount(total)
                .customers(list)
                .build();
    }

    private String monthStart(YearMonth ym) {
        return LocalDateTime.of(ym.getYear(), ym.getMonth(), 1, 0, 0, 0).format(DT);
    }

    private String monthEndExclusive(YearMonth ym) {
        YearMonth next = ym.plusMonths(1);
        return LocalDateTime.of(next.getYear(), next.getMonth(), 1, 0, 0, 0).format(DT);
    }

    private double rate(int numerator, int denom) {
        if (denom <= 0) {
            return 0.0;
        }
        return round1((double) numerator / denom * 100.0);
    }

    private double round1(double round) {
        return Math.round(round * 10.0) / 10.0;
    }


}
