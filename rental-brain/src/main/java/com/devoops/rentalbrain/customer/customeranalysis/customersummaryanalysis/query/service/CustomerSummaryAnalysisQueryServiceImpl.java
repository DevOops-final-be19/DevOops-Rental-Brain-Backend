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

        String curFrom = curYm.atDay(1).atStartOfDay().toString();
        String curTo = curYm.plusMonths(1).atDay(1).atStartOfDay().toString();

        String prevFrom = prevYm.atDay(1).atStartOfDay().toString();
        String prevTo = prevYm.plusMonths(1).atDay(1).atStartOfDay().toString();


        int totalCustomers = customerSummaryAnalysisQueryMapper.countTotalCustomers();

        // segment_id별 고객 수
        int curPotential = customerSummaryAnalysisQueryMapper.countCustomersBySegmentId(POTENTIAL_SEGMENT_ID);
        int curBlacklist = customerSummaryAnalysisQueryMapper.countCustomersBySegmentId(BLACKLIST_SEGMENT_ID);

        // 거래 고객 수 = 전체 - 잠재 - 블랙리스트
        int curTradeCustomers = Math.max(totalCustomers - curPotential - curBlacklist, 0);


        // 임시(스냅샷이 없을 때): 전월 대비는 0으로 두고, 추후 스냅샷 도입 시 교체
        int prevTradeCustomers = 0;
        int tradeMomDiff = curTradeCustomers - prevTradeCustomers;
        double tradeMomRate = 0.0;

        // KPI 2) 평균 거래액(월) - monthly_payment 기반
        long curAvgTrade = safeLong(customerSummaryAnalysisQueryMapper.avgMonthlyPaymentByMonth(curFrom, curTo));
        long prevAvgTrade = safeLong(customerSummaryAnalysisQueryMapper.avgMonthlyPaymentByMonth(prevFrom, prevTo));
        double avgTradeMomRate = momRate(prevAvgTrade, curAvgTrade);

        // KPI 3) 평균 만족도(월)
        double curStar = safeDouble(customerSummaryAnalysisQueryMapper.avgStarByMonth(curFrom, curTo));
        double prevStar = safeDouble(customerSummaryAnalysisQueryMapper.avgStarByMonth(prevFrom, prevTo));
        double avgStarMomDiff = round1(curStar - prevStar);

        // KPI 4/5) 안정/위험 (거래 고객 기준으로 계산)

        // 위험 고객 수는 snapshot 기반(월별 비교 가능)
        int curRisk = customerSummaryAnalysisQueryMapper.countMonthRiskCustomers(month);
        int prevRisk = customerSummaryAnalysisQueryMapper.countMonthRiskCustomers(prevMonth);

        // 위험률 분모 = 거래 고객 수
        double curRiskRate = rate(curRisk, curTradeCustomers);

        double prevRiskRate = rate(prevRisk, curTradeCustomers);

        double riskMomDiffRate = round1(curRiskRate - prevRiskRate); // %p

        // 안정 고객은 거래 고객 안에서 위험을 제외한 고객
        int stableCount = Math.max(curTradeCustomers - curRisk, 0);
        double stableRate = rate(stableCount, curTradeCustomers); // 안정 비율(%)

        return CustomerSummaryAnalysisQueryKPIDTO.builder()
                .currentMonth(month)
                .previousMonth(prevMonth)

                // KPI 1
                .tradeCustomerCount(curTradeCustomers)
                .totalCustomerCount(totalCustomers)
                .potentialCustomerCount(curPotential)
                .blacklistCustomerCount(curBlacklist)

                // 전월 대비 (스냅샷 없으면 임시 0 처리)
                .prevTradeCustomerCount(prevTradeCustomers)
                .tradeCustomerMomDiff(tradeMomDiff)
                .tradeCustomerMomRate(tradeMomRate)

                // KPI 2
                .avgTradeAmount(curAvgTrade)
                .avgTradeMomRate(avgTradeMomRate)

                // KPI 3
                .avgStar(curStar)
                .avgStarMomDiff(avgStarMomDiff)

                // KPI 4
                .stableCustomerCount(stableCount)
                .stableCustomerRate(stableRate)

                // KPI 5
                .riskCustomerCount(curRisk)
                .riskRate(curRiskRate)
                .riskMomDiffRate(riskMomDiffRate)
                .build();
    }


    // Kpi 이탈 위험률 조회
    @Override
    public ChurnKpiCardResponseDTO getRiskKpi(String month) {

        // 전체 수 조회
        int total = customerSummaryAnalysisQueryMapper.countTotalCustomers();


        YearMonth yearMonth = YearMonth.parse(month);
        String prevMonth = yearMonth.minusMonths(1).toString();

        int curRisk = customerSummaryAnalysisQueryMapper.countMonthRiskCustomers(month);
        int prevRisk = customerSummaryAnalysisQueryMapper.countMonthRiskCustomers(prevMonth);

        double curRiskRate = rate(curRisk, total);
        double prevRiskRate = rate(prevRisk, total);

        int retained = Math.max(total - curRisk, 0);
        double retentionRate = round1(100.0 - curRiskRate);

        return ChurnKpiCardResponseDTO.builder()
                .snapshotMonth(month)
                .prevMonth(prevMonth)
                .totalCustomerCount(total)
                .curRiskCustomerCount(curRisk)
                .curRiskRate(curRiskRate)
                .prevRiskCustomerCount(prevRisk)
                .prevRiskRate(prevRiskRate)
                .momDiffRate(round1(curRiskRate - prevRiskRate)) // %p
                .curRetainedCustomerCount(retained)
                .curRetentionRate(retentionRate)
                .build();
    }


    // 차트 (월별 위험률 차트)
    @Override
    public List<MonthlyRiskRateResponseDTO> getMonthlyRiskRate(String fromMonth, String toMonth) {
        int total = customerSummaryAnalysisQueryMapper.countTotalCustomers();

        List<String> months = customerSummaryAnalysisQueryMapper.findMonthsBetween(fromMonth, toMonth);
        List<MonthlyRiskRateResponseDTO> result = new ArrayList<>();

        for (String month : months) {
            int risk = customerSummaryAnalysisQueryMapper.countMonthRiskCustomers(month);

            result.add(MonthlyRiskRateResponseDTO.builder()
                    .snapshotMonth(month)
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


