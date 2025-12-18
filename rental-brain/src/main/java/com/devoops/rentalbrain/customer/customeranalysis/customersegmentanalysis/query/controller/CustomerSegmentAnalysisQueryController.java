//package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.controller;
//
//import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentAnalysisQueryKPI1DTO;
//import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentAnalysisQueryKPI2DTO;
//import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.service.CustomerSegmentAnalysisQueryService;
//import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentAnalysisQueryKPIDTO;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/customersegmentanalysis")
//@Tag(
//        name = "고객 세그먼트 분석 조회(Query)",
//        description = "고객 세그먼트 분석 조회 및 상세 조회 API"
//)
//public class CustomerSegmentAnalysisQueryController {
//
//    private final CustomerSegmentAnalysisQueryService customerSegmentAnalysisQueryservice;
//
//    @Autowired
//    public CustomerSegmentAnalysisQueryController(CustomerSegmentAnalysisQueryService customerSegmentAnalysisQueryservice) {
//        this.customerSegmentAnalysisQueryservice = customerSegmentAnalysisQueryservice;
//    }
//
//    @GetMapping("/health")
//    public String health() {
//        return "CustomerSegmentAnalysis OK";
//    }
//
//    @Operation(
//            summary = "세그먼트 분석 kpi",
//            description = "고객 세그먼트 분석에 들어가는 kpi 카드 입니다."
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "세그먼트 kpi 조회 성공"),
//            @ApiResponse(responseCode = "404", description = "세그먼트 kpi 정보 없음"),
//            @ApiResponse(responseCode = "500", description = "서버 오류")
//    })
//
//    // postman으로 테스트할때 2025-02 이런식으로 MM 두자리로 해야함
//    @GetMapping("/kpi1")
//    public ResponseEntity<CustomerSegmentAnalysisQueryKPI1DTO> getCustomerSegmentAnalysisKpi1(
//            @RequestParam(required = false) String month        // 지금 세그먼트 kpi를 월별
//    ){
//        CustomerSegmentAnalysisQueryKPI1DTO kpi1
//                = customerSegmentAnalysisQueryservice.selectCustomerSegmentAnalysisKpi1(month);
//
//        return ResponseEntity.ok(kpi1);
//    }
//
//
//
////    @GetMapping("/kpi2")
////    public ResponseEntity<CustomerSegmentAnalysisQueryKPI2DTO> getCustomerSegmentAnalysisKpi2(
////            @RequestParam(required = false) String month        // 지금 세그먼트 kpi를 월별
////    ){
////        CustomerSegmentAnalysisQueryKPI2DTO kpi2
////                = customerSegmentAnalysisQueryservice.selectCustomerSegmentAnalysisKpi2(month);
////
////        return ResponseEntity.ok(kpi2);
////    }
//}
