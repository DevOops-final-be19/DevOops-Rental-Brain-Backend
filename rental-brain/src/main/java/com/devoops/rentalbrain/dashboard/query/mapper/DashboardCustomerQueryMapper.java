package com.devoops.rentalbrain.dashboard.query.mapper;

import com.devoops.rentalbrain.dashboard.query.dto.QuarterlyCustomerTrendItemDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface DashboardCustomerQueryMapper {

    // 분기별 고객 트렌드
    List<QuarterlyCustomerTrendItemDTO> selectQuarterlyCustomerTrend(
            @Param("year") int year
    );

    // 향후 60일 만료 예정 계약 수
    int countExpiringContractsNext60(
            @Param("now") LocalDateTime now,
            @Param("until") LocalDateTime until
    );

    // 납부 연체(진행중)
    int countPayOverdueInProgress();

    // 문의 대기
    int countWaitingInquiries();

    // 월 매출 합 (월 과금 기준, MRR)
    // Service에서 yyyy-MM-dd HH:mm:ss 문자열로 넘김
    long sumMonthlyRevenueBetween(@Param("start") LocalDateTime start,
                                   @Param("end") LocalDateTime end);
}