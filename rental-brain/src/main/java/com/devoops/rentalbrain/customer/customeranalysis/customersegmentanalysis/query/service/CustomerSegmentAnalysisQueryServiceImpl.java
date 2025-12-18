//package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.service;
//
//import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentAnalysisQueryKPI1DTO;
//import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentAnalysisQueryKPI2DTO;
//import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentAnalysisQueryKPIDTO;
//import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.mapper.CustomerSegmentAnalysisQueryMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@Slf4j
//public class CustomerSegmentAnalysisQueryServiceImpl implements CustomerSegmentAnalysisQueryService {
//
//    private final CustomerSegmentAnalysisQueryMapper customerSegmentAnalysisQueryMapper;
//
//
//    @Autowired
//    public CustomerSegmentAnalysisQueryServiceImpl(CustomerSegmentAnalysisQueryMapper customerSegmentAnalysisQueryMapper) {
//        this.customerSegmentAnalysisQueryMapper = customerSegmentAnalysisQueryMapper;
//    }
//
//    // 세그먼트 kpi 조회
//    @Override
//    public CustomerSegmentAnalysisQueryKPI1DTO selectCustomerSegmentAnalysisKpi(String month) {
//        // month 파라미터 없으면 이번달로 처리
//        // month = "YYYY-MM" = 4자리 년도-두자리 월
//
//        // month 파라미터가 없으면 → 이번 달 KPI OR 있으면 → "YYYY-MM" 형식으로 변환
//        java.time.YearMonth ym = (month == null || month.isBlank())
//                ? java.time.YearMonth.now()
//                : java.time.YearMonth.parse(month);
//
//
//        // 이번 달 기간 계산 (1일 00:00:00 ~ 마지막일 23:59:00)
//        java.time.LocalDateTime monthStart = ym.atDay(1).atStartOfDay();
//        java.time.LocalDateTime monthEnd = ym.atEndOfMonth().atTime(23, 59, 59);
//
//        // 전월(비교용) 기간 계산
//        java.time.YearMonth prevYm = ym.minusMonths(1);
//        java.time.LocalDateTime prevStart = prevYm.atDay(1).atStartOfDay();
//        java.time.LocalDateTime prevEnd = prevYm.atEndOfMonth().atTime(23, 59, 59);
//
//
//        // 카드1
//        CustomerSegmentAnalysisQueryKPI1DTO kpi = customerSegmentAnalysisQueryMapper.selectSegmentKpiBase(
//                monthStart, monthEnd, prevStart, prevEnd, "이탈 위험 고객"
//        );
//
//        // 카드2
//        List<CustomerSegmentAnalysisQueryKPI2DTO.DistItem> dist =
//                customerSegmentAnalysisQueryMapper.selectRiskReasonDist(
//                        monthStart, monthEnd, "이탈 위험 고객");
//
//        kpi.setRiskReasonDist(dist);
//
//        log.info("세그먼트 KPI 조회 - month: {}, dto: {}", ym, kpi);
//        return kpi;
//    }
//
//}
