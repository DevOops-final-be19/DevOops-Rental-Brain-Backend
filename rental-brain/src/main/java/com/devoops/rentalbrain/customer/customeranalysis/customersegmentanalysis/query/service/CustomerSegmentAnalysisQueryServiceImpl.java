package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.service;

import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.*;
import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.mapper.CustomerSegmentAnalysisQueryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CustomerSegmentAnalysisQueryServiceImpl implements CustomerSegmentAnalysisQueryService{

    private final CustomerSegmentAnalysisQueryMapper customerSegmentAnalysisQueryMapper;
    private static final int RISK_SEGMENT_ID = 4;

    @Autowired
    public CustomerSegmentAnalysisQueryServiceImpl(CustomerSegmentAnalysisQueryMapper customerSegmentAnalysisQueryMapper) {
        this.customerSegmentAnalysisQueryMapper = customerSegmentAnalysisQueryMapper;
    }

    // 고객 세그먼트 분석 kpi 1번
    @Override
    public CustomerSegmentAnalysisRiskKPIDTO getRiskKpi(String month) {
        YearMonth currentMonth = YearMonth.parse(month);
        String previousMonth = currentMonth.minusMonths(1).toString();

        int totalCustomers = customerSegmentAnalysisQueryMapper.countTotalCustomers();
        int currentRiskCustomers = customerSegmentAnalysisQueryMapper.countCurrentRiskCustomers(RISK_SEGMENT_ID);

        // 전월 대비에 대한 것은 Snapshot 기반으로
        int currentSnapshotRisk = customerSegmentAnalysisQueryMapper.countSnapshotRiskCustomers(month);
        int previousSnapshotRisk = customerSegmentAnalysisQueryMapper.countSnapshotRiskCustomers(previousMonth);

        double currentRiskRate = rate(currentRiskCustomers, totalCustomers);
        double currentSnapshotRate = rate(currentSnapshotRisk, totalCustomers);
        double previousSnapshotRate = rate(previousSnapshotRisk, totalCustomers);


        return CustomerSegmentAnalysisRiskKPIDTO.builder()
                .currentMonth(month)
                .previousMonth(previousMonth)
                .totalCustomerCount(totalCustomers)
                .currentRiskCustomerCount(currentRiskCustomers)
                .currentRiskRate(currentRiskRate)
                .momDiffRate(round1(currentSnapshotRate - previousSnapshotRate))
                .build();

    }

    // 고객 세그먼트 분석 kpi 2번
    @Override
    public List<CustomerSegmentAnalysisRiskReaseonKPIDTO> getRiskReasonKpi(String month) {

        if (month == null || month.isBlank()) {
            throw new IllegalArgumentException("month 파라미터는 필수입니다. 예: 2025-02");
        }

        YearMonth ym = YearMonth.parse(month);

        // from/to 범위: (from, to)
        String from = ym.atDay(1).atStartOfDay().toString();
        String to   = ym.plusMonths(1).atDay(1).atStartOfDay().toString();

        // DB 집계 결과 (있는 reasonCode만 나옴)
        List<Map<String, Object>> rows =
                customerSegmentAnalysisQueryMapper.countRiskReasonsByMonth(RISK_SEGMENT_ID, from, to);

        // KPI 카드에 항상 보여줄 4개 reasonCode를 0으로 기본 세팅
        // (순서도 고정됨)
        List<String> fixedCodes = List.of("EXPIRING", "LOW_SAT", "OVERDUE", "NO_RENEWAL");

        // code -> count 맵
        java.util.Map<String, Integer> countMap = new java.util.LinkedHashMap<>();
        for (String code : fixedCodes) countMap.put(code, 0);

        // DB 결과 반영
        for (Map<String, Object> r : rows) {
            String code = (String) r.get("reasonCode");
            int cnt = ((Number) r.get("cnt")).intValue();

            // 고정 코드만 반영 (혹시 다른 코드가 들어와도 무시)
            if (countMap.containsKey(code)) {
                countMap.put(code, cnt);
            }
        }

        // 총합 계산 (고정 4개 합)
        int total = countMap.values().stream().mapToInt(Integer::intValue).sum();

        // 최종 결과(List) 생성: 항상 4개
        List<CustomerSegmentAnalysisRiskReaseonKPIDTO> result = new java.util.ArrayList<>();
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


    // 이탈 사유 고객
    @Override
    public CustomerSegmentAnalysisRiskReasonCustomersListDTO getRiskReasonCustomers(String month, String reasonCode) {

        if (month == null || month.isBlank()) {
            throw new IllegalArgumentException("month 파라미터는 필수입니다. 예: 2025-02");
        }
        if (reasonCode == null || reasonCode.isBlank()) {
            throw new IllegalArgumentException("reasonCode 파라미터는 필수입니다.");
        }

        // KPI랑 동일하게 고정 코드만 허용
        List<String> fixedCodes = List.of("EXPIRING", "LOW_SAT", "OVERDUE", "NO_RENEWAL");
        if (!fixedCodes.contains(reasonCode)) {
            throw new IllegalArgumentException("허용되지 않는 reasonCode 입니다: " + reasonCode);
        }

        YearMonth ym = YearMonth.parse(month);
        String from = ym.atDay(1).atStartOfDay().toString();
        String to   = ym.plusMonths(1).atDay(1).atStartOfDay().toString();

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








    private double rate(int numerator, int denom) {
        if(denom <= 0) {
            return 0.0;
        }
        return round1((double)numerator / denom * 100.0);
    }

    private double round1(double round) {
        return Math.round(round * 10.0) / 10.0;
    }


    @Override
    public List<CustomerSegmentTradeChartDTO> getSegmentTradeChart(String month) {

        if(month == null || month.isBlank()) {
            throw new IllegalArgumentException("month 파라미터는 필수 입니다. 예: YYYY-MM");
        }

        YearMonth yearMonth = YearMonth.parse(month);
        String from = yearMonth.atDay(1).atStartOfDay().toString();
        String to = yearMonth.plusMonths(1).atDay(1).atStartOfDay().toString();


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

}
