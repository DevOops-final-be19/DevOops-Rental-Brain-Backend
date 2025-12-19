package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.controller;

import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentAnalysisRiskKPIDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentAnalysisRiskReaseonKPIDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.service.CustomerSegmentAnalysisQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customersegmentanalysis")
@Tag(
        name = "고객 세그먼트 분석 조회(Query)",
        description = "고객 세그먼트 분석 조회 및 상세 조회 API"
)
public class CustomerSegmentAnalysisQueryController {

    private final CustomerSegmentAnalysisQueryService customerSegmentAnalysisQueryservice;

    @Autowired
    public CustomerSegmentAnalysisQueryController(CustomerSegmentAnalysisQueryService customerSegmentAnalysisQueryservice) {
        this.customerSegmentAnalysisQueryservice = customerSegmentAnalysisQueryservice;
    }

    @GetMapping("/health")
    public String health() {
        return "CustomerSegmentAnalysis OK";
    }


    // postman으로 테스트할때 2025-02 이런식으로 MM 두자리로 해야함
    @GetMapping("/riskKpi")
    public ResponseEntity<CustomerSegmentAnalysisRiskKPIDTO> getRiskKpi(
            @RequestParam(required = false) String month        // 지금 세그먼트 kpi를 월별
    ){
        CustomerSegmentAnalysisRiskKPIDTO kpi
                = customerSegmentAnalysisQueryservice.getRiskKpi(month);

        return ResponseEntity.ok(kpi);
    }

    @GetMapping("/riskReasonKpi")
    public ResponseEntity<List<CustomerSegmentAnalysisRiskReaseonKPIDTO>> getRiskReasonKpi(
            @RequestParam String month
    ){
        List<CustomerSegmentAnalysisRiskReaseonKPIDTO> kpis
                = customerSegmentAnalysisQueryservice.getRiskReasonKpi(month);

        return ResponseEntity.ok(kpis);
    }
}
