package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.mapper;

import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentAnalysisRiskReasonCustomerDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentDetailCardDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentTradeChartDTO;
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

    // kpi 고객 리스트
    List<CustomerSegmentAnalysisRiskReasonCustomerDTO> findRiskReasonCustomersByMonth(
            @Param("riskSegmentId") int riskSegmentId,
            @Param("reasonCode") String reasonCode,
            @Param("from") String from,
            @Param("to") String to
    );

    // 필터 넣는것
    List<Map<String, Object>> countRiskReasonsByMonth(@Param("riskSegmentId") int riskSegmentId,
                                                       @Param("from") String from,
                                                       @Param("to") String to
    );

    // 고객 세그먼트 분석 차트

    List<CustomerSegmentTradeChartDTO> getSegmentTradeChart(
                                                            @Param("from") String from,
                                                            @Param("to") String to
    );

    // 고객 세그먼트 분석 Card

    CustomerSegmentDetailCardDTO getSegmentDetailBase(@Param("segmentId") long segmentId);

    Double getSegmentAvgStar(@Param("segmentId") long segmentId);

    String getTopItemName(@Param("segmentId") long segmentId);

    String getTopSupport(@Param("segmentId") long segmentId);

    String getTopFeedback(@Param("segmentId") long segmentId);



}
