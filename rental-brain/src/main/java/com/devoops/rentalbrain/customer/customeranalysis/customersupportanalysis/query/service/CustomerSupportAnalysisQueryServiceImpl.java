package com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.service;

import com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.dto.*;
import com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.mapper.CustomerSupportAnalysisQueryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CustomerSupportAnalysisQueryServiceImpl implements CustomerSupportAnalysisQueryService {

    private final CustomerSupportAnalysisQueryMapper mapper;

    @Autowired
    public CustomerSupportAnalysisQueryServiceImpl(CustomerSupportAnalysisQueryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public CustomerSupportAnalysisQueryResponseKPIDTO getKpi(String targetMonth) {

        // ===== 기준 날짜 =====
        LocalDate base = LocalDate.parse(targetMonth + "-01");

        LocalDateTime currStart = base.atStartOfDay();
        LocalDateTime currEnd   = base.plusMonths(1).atStartOfDay();

        LocalDateTime prevStart = base.minusMonths(1).atStartOfDay();
        LocalDateTime prevEnd   = base.atStartOfDay();

        LocalDateTime ytdStart =
                LocalDate.of(base.getYear(), 1, 1).atStartOfDay();

        // =====================================================
        // KPI 1️ 총 응대 건수 (유형별 + YTD + 전월 대비)
        // =====================================================

        // 이번달 / 전월
        long qCurr = mapper.countQuotes(currStart, currEnd);
        long qPrev = mapper.countQuotes(prevStart, prevEnd);

        long iCurr = mapper.countSupport(currStart, currEnd);
        long iPrev = mapper.countSupport(prevStart, prevEnd);

        long fCurr = mapper.countFeedbacks(currStart, currEnd);
        long fPrev = mapper.countFeedbacks(prevStart, prevEnd);

        // YTD
        long qYtd = mapper.countQuotes(ytdStart, currEnd);
        long iYtd = mapper.countSupport(ytdStart, currEnd);
        long fYtd = mapper.countFeedbacks(ytdStart, currEnd);

        long ytdTotal = qYtd + iYtd + fYtd;

        CustomerSupportAnalysisQueryKPICardDTO totalResponseCard =
                CustomerSupportAnalysisQueryKPICardDTO.builder()
                        .title("총 응대 건수")
                        .subtitle("전월 대비 변화 (유형별)")
                        .ytdTotal(ytdTotal)
                        .ytdQuote(qYtd)
                        .ytdInquiry(iYtd)
                        .ytdFeedback(fYtd)
                        .typeStats(List.of(
                                typeRow("QUOTE", qCurr, qPrev),
                                typeRow("INQUIRY", iCurr, iPrev),
                                typeRow("FEEDBACK", fCurr, fPrev)
                        ))
                        .build();

        // =====================================================
        // KPI 2️ 응대 효율 (완료율 + 평균 응대 시간)
        // =====================================================

        long inquiryDoneCurr = mapper.countSupportDone(currStart, currEnd);
        long inquiryDonePrev = mapper.countSupportDone(prevStart, prevEnd);

        long feedbackDoneCurr = mapper.countFeedbackDone(currStart, currEnd);
        long feedbackDonePrev = mapper.countFeedbackDone(prevStart, prevEnd);

        long totalCurr = qCurr + iCurr + fCurr;
        long totalPrev = qPrev + iPrev + fPrev;

        // 완료율: "완료" 정의가 지금 코드상 quote는 전부 완료로 보며, support/feedback은 action NOT NULL만 완료
        double completionRateCurr =
                totalCurr == 0 ? 0.0 : (qCurr + inquiryDoneCurr + feedbackDoneCurr) * 100.0 / totalCurr;

        double completionRatePrev =
                totalPrev == 0 ? 0.0 : (qPrev + inquiryDonePrev + feedbackDonePrev) * 100.0 / totalPrev;

        double avgTimeCurr = nvl(mapper.avgQuoteProcessingTime(currStart, currEnd));
        double avgTimePrev = nvl(mapper.avgQuoteProcessingTime(prevStart, prevEnd));

        CustomerSupportAnalysisQueryEfficiencyKPIDTO efficiency =
                CustomerSupportAnalysisQueryEfficiencyKPIDTO.builder()
                        .completionRate(round1(completionRateCurr))
                        .completionRateMomP(round1(completionRateCurr - completionRatePrev)) // %p 차이
                        .avgResponseTime(round1(avgTimeCurr))
                        .avgResponseTimeMomPercent(momPercent(avgTimeCurr, avgTimePrev))     // % 변화율
                        .build();

        // =====================================================
        // KPI 3️ 고객 만족도 지수
        // =====================================================

        double avgScoreCurr = nvl(mapper.avgFeedbackStar(currStart, currEnd));
        double avgScorePrev = nvl(mapper.avgFeedbackStar(prevStart, prevEnd));

        long starTotalCurr = mapper.countFeedbackStarTotal(currStart, currEnd);
        long starTotalPrev = mapper.countFeedbackStarTotal(prevStart, prevEnd);

        long lowCurr = mapper.countFeedbackLowStar(currStart, currEnd, 2.5);
        long lowPrev = mapper.countFeedbackLowStar(prevStart, prevEnd, 2.5);

        double lowRatioCurr =
                starTotalCurr == 0 ? 0.0 : lowCurr * 100.0 / starTotalCurr;

        double lowRatioPrev =
                starTotalPrev == 0 ? 0.0 : lowPrev * 100.0 / starTotalPrev;

        CustomerSupportAnalysisQuerySatisfactionKPIDTO satisfaction =
                CustomerSupportAnalysisQuerySatisfactionKPIDTO.builder()
                        .avgScore(round1(avgScoreCurr))
                        .avgScoreMom(round1(avgScoreCurr - avgScorePrev))          // 점수 차이
                        .lowScoreRatio(round1(lowRatioCurr))
                        .lowScoreRatioMomP(round1(lowRatioCurr - lowRatioPrev))    // %p 차이
                        .build();

        // RESPONSE
        return CustomerSupportAnalysisQueryResponseKPIDTO.builder()
                .targetMonth(targetMonth)
                .previousMonth(base.minusMonths(1).toString().substring(0, 7))
                .totalResponseCard(totalResponseCard)
                .efficiency(efficiency)
                .satisfaction(satisfaction)
                .build();
    }


    // Helpers


    private CustomerSupportAnalysisQueryTypeCountDTO typeRow(
            String type, long curr, long prev
    ) {
        return CustomerSupportAnalysisQueryTypeCountDTO.builder()
                .type(type)
                .currentCount(curr)
                .previousCount(prev)
                .deltaCount(curr - prev)
                .momPercent(momPercent(curr, prev)) // prev=0이면 0.0, null 없음
                .build();
    }


    /* MoM 변화율(%) = (curr-prev)/prev * 100  / prev=0이면 0.0 */
    private double momPercent(long curr, long prev) {
        if (prev == 0) return 0.0;
        return round1(((curr - prev) * 100.0) / (double) prev);
    }

    private double momPercent(double curr, double prev) {
        if (prev == 0.0) return 0.0;
        return round1(((curr - prev) * 100.0) / prev);
    }

    /* 평균 등 Double로 올 수 있는 값 null 방지 */
    private double nvl(Double v) {
        return v == null ? 0.0 : v;
    }

    private double round1(double v) {
        return Math.round(v * 10.0) / 10.0;
    }

    // 고객 응대 분석 트랜드 차트
    @Override
    public CustomerSupportAnalysisMonthlyTrendResponseDTO getMonthlyTrend(int year) {

        List<CustomerSupportAnalysisMonthlyTrendDTO> rows = mapper.selectMonthlyTrend(year);

        return CustomerSupportAnalysisMonthlyTrendResponseDTO.builder()
                .year(year)
                .monthly(rows)
                .build();
    }
}
