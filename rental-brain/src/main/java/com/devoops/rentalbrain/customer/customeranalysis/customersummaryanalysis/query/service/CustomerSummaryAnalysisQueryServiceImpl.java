package com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.service;

import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.common.pagination.Pagination;
import com.devoops.rentalbrain.common.pagination.PagingButtonInfo;
import com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.dto.*;
import com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.mapper.CustomerSummaryAnalysisQueryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerSummaryAnalysisQueryServiceImpl implements CustomerSummaryAnalysisQueryService {

    private final CustomerSummaryAnalysisQueryMapper customerSummaryAnalysisQueryMapper;

    @Autowired
    public CustomerSummaryAnalysisQueryServiceImpl(CustomerSummaryAnalysisQueryMapper customerSummaryAnalysisQueryMapper) {
        this.customerSummaryAnalysisQueryMapper = customerSummaryAnalysisQueryMapper;
    }


    // kpi 카드들
    @Override
    public CustomerSummaryAnalysisQueryKPIDTO getkpi(String month) {

        if (month == null || month.isBlank()) {
            throw new IllegalArgumentException("month 파라미터는 필수입니다. 예: 2025-08");
        }

        final int POTENTIAL_SEGMENT_ID = 1;   // 잠재 고객
//        final int RISK_SEGMENT_ID = 4;        // 이탈 위험 고객, 여기서는 잠재랑 블랙만 제외
        final int BLACKLIST_SEGMENT_ID = 6;   // 블랙리스트 고객

        YearMonth curYm = YearMonth.parse(month);
        YearMonth prevYm = curYm.minusMonths(1);
        String prevMonth = prevYm.toString();

        // 월 범위 (거래액/만족도용)
        String curFrom  = curYm.atDay(1).atStartOfDay().toString();
        String curTo    = curYm.plusMonths(1).atDay(1).atStartOfDay().toString();
        String prevFrom = prevYm.atDay(1).atStartOfDay().toString();
        String prevTo   = prevYm.plusMonths(1).atDay(1).atStartOfDay().toString();

    /* =========================================================
       1️⃣ 전체 고객 기준 분모 (customer 테이블 기준)
       ========================================================= */
        int totalCustomers = customerSummaryAnalysisQueryMapper.countTotalCustomers(); // ← 61명
        int curPotential   = customerSummaryAnalysisQueryMapper.countCustomersBySegmentId(POTENTIAL_SEGMENT_ID);
        int curBlacklist   = customerSummaryAnalysisQueryMapper.countCustomersBySegmentId(BLACKLIST_SEGMENT_ID);

        // 거래 고객 = 전체 - 잠재 - 블랙
        int curTradeCustomers = Math.max(totalCustomers - curPotential - curBlacklist, 0);

        // 전월 비교는 "거래 고객 기준" 문구 유지용 → 현재 기준으로 동일 값 사용
        int prevTradeCustomers = curTradeCustomers;
        int tradeMomDiff = 0;
        double tradeMomRate = 0.0;

    /* =========================================================
       2️⃣ 평균 거래액 (월 기준)
       ========================================================= */
        long curAvgTrade  = safeLong(customerSummaryAnalysisQueryMapper.avgMonthlyPaymentByMonth(curFrom, curTo));
        long prevAvgTrade = safeLong(customerSummaryAnalysisQueryMapper.avgMonthlyPaymentByMonth(prevFrom, prevTo));
        double avgTradeMomRate = momRate(prevAvgTrade, curAvgTrade);

    /* =========================================================
       3️⃣ 평균 만족도 (월 기준)
       ========================================================= */
        double curStar  = safeDouble(customerSummaryAnalysisQueryMapper.avgStarByMonth(curFrom, curTo));
        double prevStar = safeDouble(customerSummaryAnalysisQueryMapper.avgStarByMonth(prevFrom, prevTo));
        double avgStarMomDiff = round1(curStar - prevStar);

    /* =========================================================
       4️⃣ 이탈 위험 / 안정 고객 (월별 snapshot 기준)
       ========================================================= */
        int curRisk  = customerSummaryAnalysisQueryMapper.countMonthRiskCustomers(month);
        int prevRisk = customerSummaryAnalysisQueryMapper.countMonthRiskCustomers(prevMonth);

        // ⚠️ 분모는 반드시 전체 고객 기준
        double curRiskRate  = rate(curRisk, totalCustomers);
        double prevRiskRate = rate(prevRisk, totalCustomers);
        double riskMomDiffRate = round1(curRiskRate - prevRiskRate); // %p

        int stableCount = Math.max(totalCustomers - curRisk - curBlacklist, 0);
        double stableRate = rate(stableCount, totalCustomers);

    /* =========================================================
       5️⃣ DTO 반환
       ========================================================= */
        return CustomerSummaryAnalysisQueryKPIDTO.builder()
                .currentMonth(month)
                .previousMonth(prevMonth)

                // 고객 수
                .totalCustomerCount(totalCustomers)
                .tradeCustomerCount(curTradeCustomers)
                .potentialCustomerCount(curPotential)
                .blacklistCustomerCount(curBlacklist)

                // 거래 고객 증감
                .prevTradeCustomerCount(prevTradeCustomers)
                .tradeCustomerMomDiff(tradeMomDiff)
                .tradeCustomerMomRate(tradeMomRate)

                // 거래액
                .avgTradeAmount(curAvgTrade)
                .avgTradeMomRate(avgTradeMomRate)

                // 만족도
                .avgStar(curStar)
                .avgStarMomDiff(avgStarMomDiff)

                // 안정 / 위험
                .stableCustomerCount(stableCount)
                .stableCustomerRate(stableRate)
                .riskCustomerCount(curRisk)
                .riskRate(curRiskRate)
                .riskMomDiffRate(riskMomDiffRate)

                .build();
    }


    // Kpi 이탈 위험률 조회
    @Override
    public ChurnKpiCardResponseDTO getRiskKpi(String month) {

        YearMonth ym = YearMonth.parse(month);
        String prevMonth = ym.minusMonths(1).toString();

        int curTotal = customerSummaryAnalysisQueryMapper.countSnapshotCustomers(month);
        int prevTotal = customerSummaryAnalysisQueryMapper.countSnapshotCustomers(prevMonth);

        int curRisk = customerSummaryAnalysisQueryMapper.countMonthRiskCustomers(month);
        int prevRisk = customerSummaryAnalysisQueryMapper.countMonthRiskCustomers(prevMonth);

        double curRiskRate = rate(curRisk, curTotal);
        double prevRiskRate = rate(prevRisk, prevTotal);

        int retained = Math.max(curTotal - curRisk, 0);
        double retentionRate = round1(100.0 - curRiskRate);

        return ChurnKpiCardResponseDTO.builder()
                .snapshotMonth(month)
                .prevMonth(prevMonth)
                .totalCustomerCount(curTotal)
                .curRiskCustomerCount(curRisk)
                .curRiskRate(curRiskRate)
                .prevRiskCustomerCount(prevRisk)
                .prevRiskRate(prevRiskRate)
                .momDiffRate(round1(curRiskRate - prevRiskRate))
                .curRetainedCustomerCount(retained)
                .curRetentionRate(retentionRate)
                .build();
    }


    // 차트 (월별 위험률 차트)
    @Override
    public List<MonthlyRiskRateResponseDTO> getMonthlyRiskRate(String fromMonth, String toMonth) {

        List<String> months = customerSummaryAnalysisQueryMapper.findMonthsBetween(fromMonth, toMonth);
        List<MonthlyRiskRateResponseDTO> result = new ArrayList<>();

        for (String m : months) {
            int total = customerSummaryAnalysisQueryMapper.countSnapshotCustomers(m);
            int risk  = customerSummaryAnalysisQueryMapper.countMonthRiskCustomers(m);

            result.add(MonthlyRiskRateResponseDTO.builder()
                    .snapshotMonth(m)
                    .riskCustomerCount(risk)
                    .riskRate(rate(risk, total))
                    .build());
        }
        return result;
    }

    // 차트 만족도
    @Override
    public CustomerSummaryAnalysisQuerySatisfactionDTO getSatisfaction() {

        CustomerSummaryAnalysisQuerySatisfactionRowDTO row = customerSummaryAnalysisQueryMapper.getSatisfaction();
        if(row == null){
            return empty();
        }

        long total = row.getTotalCount();
        if (total <= 0) {
            return empty();
        }

        return CustomerSummaryAnalysisQuerySatisfactionDTO.builder()
                .star5Count(row.getStar5Count())
                .star4Count(row.getStar4Count())
                .star3Count(row.getStar3Count())
                .star2Count(row.getStar2Count())
                .star1Count(row.getStar1Count())
                .totalCount(total)
                .star5Percent(round1(row.getStar5Count() * 100.0 / total))
                .star4Percent(round1(row.getStar4Count() * 100.0 / total))
                .star3Percent(round1(row.getStar3Count() * 100.0 / total))
                .star2Percent(round1(row.getStar2Count() * 100.0 / total))
                .star1Percent(round1(row.getStar1Count() * 100.0 / total))
                .build();
    }

    // null 이면 0으로 뽑을려고
    private CustomerSummaryAnalysisQuerySatisfactionDTO empty() {
        return CustomerSummaryAnalysisQuerySatisfactionDTO.builder()
                .star5Count(0)
                .star4Count(0)
                .star3Count(0)
                .star2Count(0)
                .star1Count(0)
                .totalCount(0)
                .star5Percent(0)
                .star4Percent(0)
                .star3Percent(0)
                .star2Percent(0)
                .star1Percent(0)
                .build();
    }


    private double rate(int numerator, int denom) {
        if (denom <= 0) return 0.0;
        return round1((double) numerator / denom * 100.0);
    }

    private double momRate(long prev, long cur) {
        if (prev <= 0) return 0.0;
        return round1(((double) (cur - prev) / prev) * 100.0);
    }

    private double round1(double v) {
        return Math.round(v * 10.0) / 10.0;
    }

    private long safeLong(Long v) {
        return v == null ? 0L : v;
    }

    private double safeDouble(Double v) {
        return v == null ? 0.0 : v;
    }

    // 만족도 분포 상세 조회
    @Override
    public PageResponseDTO<CustomerSummaryAnalysisQuerySatisfactionCustomerDTO>
                        getCustomersByStarWithPaging(int star, Criteria criteria) {

        if (star < 1 || star > 5) {
            throw new IllegalArgumentException("star는 1~5만 가능합니다.");
        }

        // 1) 목록
        List<CustomerSummaryAnalysisQuerySatisfactionCustomerDTO> contents =
                customerSummaryAnalysisQueryMapper.selectCustomersByStarWithPaging(
                        star,
                        criteria.getOffset(),
                        criteria.getAmount()
                );

        // 2) 전체 건수
        long totalCount = customerSummaryAnalysisQueryMapper.countCustomersByStar(star);

        // 3) 페이지 버튼 정보
        PagingButtonInfo paging = Pagination.getPagingButtonInfo(criteria, totalCount);

        return new PageResponseDTO<>(contents, totalCount, paging);
    }

    // 고객 요약 분석 세그먼트 원형 차트
    public CustomerSegmentDistributionResponseDTO getSegmentDistribution() {

        List<CustomerSegmentDistributionDTO> distribution =
                customerSummaryAnalysisQueryMapper.selectCustomerSegmentDistribution();

        long total = distribution.stream()
                .mapToLong(CustomerSegmentDistributionDTO::getCustomerCount)
                .sum();

        if (total <= 0) {
            return CustomerSegmentDistributionResponseDTO.builder()
                    .totalCustomerCount(0)
                    .segments(distribution)
                    .build();
        }

        for (CustomerSegmentDistributionDTO distributionPercent : distribution) {
            double percent = round1(distributionPercent.getCustomerCount() * 100.0 / total);
            distributionPercent.setCountPercent(percent); // setter 없으면 builder로 새로 만들어도 됨
        }

        return CustomerSegmentDistributionResponseDTO.builder()
                .totalCustomerCount(total)
                .segments(distribution)
                .build();
    }


  }


