package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CustomerSegmentAnalysisQueryMapper {

    // kpi 1번
    int countTotalCustomers();
    int countCurrentRiskCustomers(@Param("riskSegmentId") int riskSegmentId);

    // 전월 비교
    int countSnapshotRiskCustomers(@Param("snapshotMonth") String snapshotMonth);

    // kpi 2번
    List<Map<String, Object>> countRiskReasons(@Param("riskSegmentId") int riskSegmentId);

    // 필터 넣는것
    List<Map<String, Object>> countRiskReasonsByMonth(@Param("riskSegmentId") int riskSegmentId,
                                                       @Param("from") String from,
                                                       @Param("to") String to);



}
