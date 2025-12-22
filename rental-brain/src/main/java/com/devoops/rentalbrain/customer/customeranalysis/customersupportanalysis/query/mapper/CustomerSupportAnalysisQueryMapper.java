package com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.mapper;

import com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.dto.CustomerSupportAnalysisMonthlyTrendDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CustomerSupportAnalysisQueryMapper {

    // KPI1: 유형별 건수
    long countQuotes(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    long countSupport(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    long countFeedbacks(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // KPI2: 완료율 (문의/피드백 완료건)
    long countSupportDone(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    long countFeedbackDone(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // KPI2: 평균 응대 시간(견적 processing_time)
    Double avgQuoteProcessingTime(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // KPI3: 만족도 평균/저만족비중
    Double avgFeedbackStar(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    long countFeedbackStarTotal(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    long countFeedbackLowStar(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                              @Param("threshold") double threshold); // 2.5


    // 고객 응대 분석 트랜드 차트
    List<CustomerSupportAnalysisMonthlyTrendDTO> selectMonthlyTrend(@Param("year") int year);

}
