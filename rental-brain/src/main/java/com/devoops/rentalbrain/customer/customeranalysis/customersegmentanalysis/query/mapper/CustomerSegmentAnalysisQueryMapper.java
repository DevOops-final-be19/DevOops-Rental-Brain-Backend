package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.mapper;

import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentAnalysisRiskReasonCustomerDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentDetailCardDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentRiskCustomerDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentTradeChartDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CustomerSegmentAnalysisQueryMapper {

    /* =========================================================
       [KPI 1 / 월별 위험률]  ✅ 월말 기준 (customer_segment_history 기반)
       - 분모: 해당 월말 기준 전체 고객 수
       - 분자: 해당 월말 기준 위험(세그먼트=4) 고객 수
       ========================================================= */

    // 월말 기준 전체 고객 수 (history)
    int countTotalCustomersAtMonth(@Param("monthEndExclusive") String monthEndExclusive);

    // 전체 고객수
    int countTotalCustomers();

    // 월말 기준 세그먼트별 고객 수 (history)
    int countCustomersBySegmentAtMonth(
            @Param("segmentId") int segmentId,
            @Param("monthEndExclusive") String monthEndExclusive
    );


    /* =========================================================
       [KPI 2] 이탈 위험 사유 분포 / 고객 리스트
       - customer_risk_transition_history 기반
       ========================================================= */

    // 전체 기간 기준 분포
    List<Map<String, Object>> countRiskReasons(@Param("riskSegmentId") int riskSegmentId);

    // 월 기준 분포
    List<Map<String, Object>> countRiskReasonsByMonth(
            @Param("riskSegmentId") int riskSegmentId,
            @Param("from") String from,
            @Param("to") String to
    );

    // 월 기준 사유별 고객 리스트
    List<CustomerSegmentAnalysisRiskReasonCustomerDTO> findRiskReasonCustomersByMonth(
            @Param("riskSegmentId") int riskSegmentId,
            @Param("reasonCode") String reasonCode,
            @Param("from") String from,
            @Param("to") String to
    );


    /* =========================================================
       [세그먼트 분석] 차트 / 상세 카드
       ========================================================= */

    // 세그먼트별 거래 차트
    List<CustomerSegmentTradeChartDTO> getSegmentTradeChart(
            @Param("from") String from,
            @Param("to") String to
    );

    // 세그먼트 상세 카드(기본)
    CustomerSegmentDetailCardDTO getSegmentDetailBase(@Param("segmentId") long segmentId);

    Double getSegmentAvgStar(@Param("segmentId") long segmentId);

    String getTopItemName(@Param("segmentId") long segmentId);

    String getTopSupport(@Param("segmentId") long segmentId);

    String getTopFeedback(@Param("segmentId") long segmentId);


    // 이탈 위험 상세 조회 할때 사용
    List<CustomerSegmentRiskCustomerDTO> findRiskCustomersAtMonth(
            @Param("monthEndExclusive") String monthEndExclusive,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    int countRiskCustomersAtMonth(
            @Param("monthEndExclusive") String monthEndExclusive
    );
}
