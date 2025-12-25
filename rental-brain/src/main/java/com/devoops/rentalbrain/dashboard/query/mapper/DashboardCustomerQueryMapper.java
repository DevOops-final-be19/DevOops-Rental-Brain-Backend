package com.devoops.rentalbrain.dashboard.query.mapper;

import com.devoops.rentalbrain.dashboard.query.dto.QuarterlyCustomerTrendItemDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface DashboardCustomerQueryMapper {
    List<QuarterlyCustomerTrendItemDTO> selectQuarterlyCustomerTrend(@Param("year") int year);


    int countExpiringContractsNext60(@Param("now") LocalDateTime now,
                                     @Param("until") LocalDateTime until);

    int countPayOverdueInProgress();

    int countWaitingInquiries();

    long sumRevenueBetween(@Param("start") LocalDateTime start,
                           @Param("end") LocalDateTime end);
}