//package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.mapper;
//
//import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentAnalysisQueryKPIDTO;
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Param;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Mapper
//public interface CustomerSegmentAnalysisQueryMapper {
//
//    // 세그먼트 분석 KPI
//    CustomerSegmentAnalysisQueryKPIDTO selectSegmentKpiBase(
//                                        @Param("monthStart") LocalDateTime monthStart,
//                                        @Param("monthEnd") LocalDateTime monthEnd,
//                                        @Param("prevStart") LocalDateTime prevStart,
//                                        @Param("prevEnd") LocalDateTime prevEnd,
//                                        // 이건 이름으로 고정했음 -> 이탈 위험 고객
//                                        @Param("riskSegmentName") String riskSegmentName);
//
//    List<CustomerSegmentAnalysisQueryKPIDTO.DistItem> selectRiskReasonDist(
//                                        @Param("monthStart") LocalDateTime monthStart,
//                                        @Param("monthEnd") LocalDateTime monthEnd,
//                                        @Param("riskSegmentName") String riskSegmentName
//    );
//
//}
