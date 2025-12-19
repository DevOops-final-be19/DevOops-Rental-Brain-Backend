package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.controller;

import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentAnalysisRiskKPIDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentAnalysisRiskReaseonKPIDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentDetailCardDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.CustomerSegmentTradeChartDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.service.CustomerSegmentAnalysisQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Operation(
            summary = "이탈 위험 KPI 조회",
            description = """
                    기준 월(month) 기준으로 이탈 위험 고객 KPI를 조회합니다.
                    - month 형식: YYYY-MM (예: 2025-02)
                    - month 미입력 시: 서비스 기본 기준월 로직(예: 현재월/최근월) 적용
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = CustomerSegmentAnalysisRiskKPIDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 형식 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<CustomerSegmentAnalysisRiskKPIDTO> getRiskKpi(
            @RequestParam(required = false) String month        // 지금 세그먼트 kpi를 월별
    ){
        CustomerSegmentAnalysisRiskKPIDTO kpi
                = customerSegmentAnalysisQueryservice.getRiskKpi(month);

        return ResponseEntity.ok(kpi);
    }

    @GetMapping("/riskReasonKpi")
    @Operation(
            summary = "이탈 위험 사유별 KPI 조회",
            description = """
                    기준 월(month)에 대해 이탈 위험 사유(reason code)별 고객 수/비중 KPI를 조회합니다.
                    - month 형식: YYYY-MM (예: 2025-02)
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = CustomerSegmentAnalysisRiskReaseonKPIDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 형식 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<List<CustomerSegmentAnalysisRiskReaseonKPIDTO>> getRiskReasonKpi(
            @RequestParam String month
    ){
        List<CustomerSegmentAnalysisRiskReaseonKPIDTO> kpis
                = customerSegmentAnalysisQueryservice.getRiskReasonKpi(month);

        return ResponseEntity.ok(kpis);
    }

    @GetMapping("/segmentTradeChart")
    @Operation(
            summary = "세그먼트별 거래 차트 조회",
            description = """
                    기준 월(month)에 대해 세그먼트별 고객 수 / 총 거래액 / 평균 거래액(고객당)을 조회합니다.
                    - month 형식: YYYY-MM (예: 2025-02)
                    - month 미입력 시: 서비스 기본 기준월 로직 적용
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = CustomerSegmentTradeChartDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 형식 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<List<CustomerSegmentTradeChartDTO>> getSegmentTradeChart(
            @RequestParam(required = false) String month
    ){
        return ResponseEntity.ok(customerSegmentAnalysisQueryservice.getSegmentTradeChart(month));
    }



    @GetMapping("/segmentCard")
    @Operation(
            summary = "세그먼트 상세 카드 조회",
            description = """
                선택한 세그먼트(segmentId)에 대한 상세 요약 카드 정보를 조회합니다.
                - 세그먼트 기본 정보
                - 고객 수 / 거래 요약 / 주요 지표
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = CustomerSegmentDetailCardDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (segmentId 누락 또는 형식 오류)"),
            @ApiResponse(responseCode = "404", description = "해당 세그먼트를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<CustomerSegmentDetailCardDTO> getCustomerSegmentDetailCard(
            @RequestParam Long segmentId
    ){
        return ResponseEntity.ok(customerSegmentAnalysisQueryservice.getSegmentDetailCard(segmentId));
    }

}
