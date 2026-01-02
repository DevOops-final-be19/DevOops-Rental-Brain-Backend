package com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.service;

import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.common.pagination.Pagination;
import com.devoops.rentalbrain.common.pagination.PagingButtonInfo;
import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.ChurnKpiCardResponseDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.MonthlyRiskRateResponseDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.dto.*;
import com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.mapper.CustomerSummaryAnalysisQueryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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


    private static final int SEGMENT_POTENTIAL = 1; // 잠재 고객
    private static final int SEGMENT_BLACKLIST = 6;  // 블랙리스트 고객

    // kpi 카드들
    @Override
    public CustomerSummaryAnalysisQueryKPIDTO getkpi(String month) {

        // 0️⃣ month 파싱
        YearMonth currentMonth = (month == null || month.isBlank())
                ? YearMonth.now()
                : YearMonth.parse(month);

        String prevMonth = currentMonth.minusMonths(1).toString();
        String curMonth = currentMonth.toString();

        // 1️⃣ 전체 고객 / 세그먼트 고객 수 (현재 기준)
        int totalCustomers = customerSummaryAnalysisQueryMapper.countTotalCustomers();
        int curPotential = customerSummaryAnalysisQueryMapper.countCustomersBySegmentId(SEGMENT_POTENTIAL);
        int curBlacklist = customerSummaryAnalysisQueryMapper.countCustomersBySegmentId(SEGMENT_BLACKLIST);

        // 2️⃣ 거래 고객 수 (현재 기준: 잠재+블랙리스트 제외)
        int curTradeCustomers = customerSummaryAnalysisQueryMapper.countTradeCustomers(
                SEGMENT_POTENTIAL, SEGMENT_BLACKLIST
        );

        // 3️⃣ 전월 대비(현재는 임시값 유지)
        int prevTradeCustomers = curTradeCustomers;
        int tradeMomDiff = 0;
        double tradeMomRate = 0.0;

        // 4️⃣ 평균 거래액(월): monthly_payment 기반 (month 구간)
        LocalDateTime curFrom = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime curTo = currentMonth.plusMonths(1).atDay(1).atStartOfDay();

        long curAvgTradeAmount = customerSummaryAnalysisQueryMapper.avgMonthlyPaymentByMonth(curFrom.toString(), curTo.toString());

        // 전월 평균 거래액
        YearMonth prevYm = currentMonth.minusMonths(1);
        LocalDateTime prevFrom = prevYm.atDay(1).atStartOfDay();
        LocalDateTime prevTo = prevYm.plusMonths(1).atDay(1).atStartOfDay();

        long prevAvgTradeAmount = customerSummaryAnalysisQueryMapper.avgMonthlyPaymentByMonth(prevFrom.toString(), prevTo.toString());

        double avgTradeMomRate = calcMomRate(curAvgTradeAmount, prevAvgTradeAmount);

        // 5️⃣ 평균 만족도(월)
        double curAvgStar = customerSummaryAnalysisQueryMapper.avgStarByMonth(curFrom.toString(), curTo.toString());
        double prevAvgStar = customerSummaryAnalysisQueryMapper.avgStarByMonth(prevFrom.toString(), prevTo.toString());

        double avgStarMomDiff = round1(curAvgStar - prevAvgStar);

        return CustomerSummaryAnalysisQueryKPIDTO.builder()
                .currentMonth(curMonth)
                .previousMonth(prevMonth)

                .tradeCustomerCount(curTradeCustomers)

                .totalCustomerCount(totalCustomers)
                .potentialCustomerCount(curPotential)
                .blacklistCustomerCount(curBlacklist)

                .prevTradeCustomerCount(prevTradeCustomers)
                .tradeCustomerMomDiff(tradeMomDiff)
                .tradeCustomerMomRate(tradeMomRate)

                .avgTradeAmount(curAvgTradeAmount)
                .avgTradeMomRate(round1(avgTradeMomRate))

                .avgStar(curAvgStar)
                .avgStarMomDiff(avgStarMomDiff)
                .build();
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

    private double calcMomRate(long current, long previous) {
        if(previous <= 0){
            return (current > 0) ? 100.0 : 0.0;
        }
        return ((double) (current -previous) / previous)  * 100.0 ;
    }

    private double round1(double v) {
        return Math.round(v * 10.0) / 10.0;
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


