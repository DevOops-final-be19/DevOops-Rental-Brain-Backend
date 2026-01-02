package com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.mapper;


import com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.dto.CustomerSegmentDistributionDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.dto.CustomerSummaryAnalysisQuerySatisfactionCustomerDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.dto.CustomerSummaryAnalysisQuerySatisfactionRowDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.dto.CustomerSupportAnalysisMonthlyTrendResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerSummaryAnalysisQueryMapper {


    // 총 고객 수 (누적)
    int countTotalCustomers();

    //  세그먼트별 고객 수 (현재 customer.segment_id 기준)
    int countCustomersBySegmentId(@Param("segmentId") int segmentId);

    //  거래 고객 수(잠재+블랙리스트 제외) SQL에서 바로 계산
    int countTradeCustomers(@Param("potentialSegmentId") int potentialSegmentId,
                            @Param("blacklistSegmentId") int blacklistSegmentId);
    // 이탈 위험률 관련
    int countMonthRiskCustomers(@Param("snapshotMonth") String snapshotMonth);
    List<String> findMonthsBetween(@Param("fromMonth") String fromMonth,
                                   @Param("toMonth") String toMonth);


    // monthly_payment 기반 "고객당 평균 월 과금액"
    Long avgMonthlyPaymentByMonth(@Param("from") String from,
                                  @Param("to") String to);

    // 월별 평균 별점(feedback 기준)
    Double avgStarByMonth(@Param("from") String from,
                          @Param("to") String to);

    // 총 고객 수 전월 대비(%) 계산용 - "월 활동 고객 수"
    int countActiveCustomersByMonth(@Param("from") String from,
                                    @Param("to") String to);

    CustomerSummaryAnalysisQuerySatisfactionRowDTO getSatisfaction();

    long countCustomersByStar(@Param("star") int star);

    List<CustomerSummaryAnalysisQuerySatisfactionCustomerDTO> selectCustomersByStarWithPaging(
                                                                                                @Param("star") int star,
                                                                                                @Param("offset") int offset,
                                                                                                @Param("limit") int limit
    );


    // 고객 요약 분석 세그먼트 원형 차트
    List<CustomerSegmentDistributionDTO> selectCustomerSegmentDistribution();

    int countTotalCustomersAtMonth(
            @Param("monthEndExclusive") String monthEndExclusive
    );

    int countCustomersBySegmentAtMonth(
            @Param("segmentId") int segmentId,
            @Param("monthEndExclusive") String monthEndExclusive
    );

    int countSnapshotCustomers(String month);
}
