package com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.controller;

import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.dto.*;
import com.devoops.rentalbrain.customer.customeranalysis.customersegmentanalysis.query.service.CustomerSegmentAnalysisQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customerSegmentAnalysis")
@Tag(
        name = "고객 세그먼트 분석 조회(Query)",
        description = "고객 세그먼트 분석 조회 및 상세 조회 API"
)
public class CustomerSegmentAnalysisQueryController {

    private final CustomerSegmentAnalysisQueryService customerSegmentAnalysisQueryService;

    @Autowired
    public CustomerSegmentAnalysisQueryController(CustomerSegmentAnalysisQueryService customerSegmentAnalysisQueryService) {
        this.customerSegmentAnalysisQueryService = customerSegmentAnalysisQueryService;
    }

    @GetMapping("/health")
    public String health() {
        return "CustomerSegmentAnalysis OK";
    }

    @Operation(
            summary = "이탈 위험 KPI 조회",
            description = """
                    기준 월(month) '월말 기준(as-of month end)'으로 이탈 위험 KPI를 조회합니다.
                    - month 형식: YYYY-MM (예: 2025-02)
                    - month 미입력 시: 현재월 기준으로 조회합니다.
                    - 위험률(%) = (해당 월말 기준 이탈 위험 고객 수 / 해당 월말 기준 전체 고객 수) * 100
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ChurnKpiCardResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 형식 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/riskKpi")
    public ResponseEntity<ChurnKpiCardResponseDTO> getRiskKpi(
            @RequestParam(required = false) String month
    ){
        return ResponseEntity.ok(customerSegmentAnalysisQueryService.getRiskKpi(month));
    }


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
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CustomerSegmentAnalysisRiskReaseonKPIDTO.class)))
            ),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 형식 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/riskReasonKpi")
    public ResponseEntity<List<CustomerSegmentAnalysisRiskReaseonKPIDTO>> getRiskReasonKpi(
            @RequestParam String month
    ){
        return ResponseEntity.ok(customerSegmentAnalysisQueryService.getRiskReasonKpi(month));
    }


    @Operation(
            summary = "이탈 위험 사유별 고객 리스트 조회",
            description = """
                기준 월(month)과 사유 코드(reasonCode)에 대해 해당 사유로 이탈 위험(세그먼트 4)으로 전환된 고객 리스트를 조회합니다.
                - month 형식: YYYY-MM (예: 2025-02)
                - reasonCode: EXPIRING | LOW_SAT | OVERDUE | NO_RENEWAL
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = CustomerSegmentAnalysisRiskReasonCustomersListDTO.class))),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 형식 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/riskReasonCustomers")
    public ResponseEntity<CustomerSegmentAnalysisRiskReasonCustomersListDTO> getRiskReasonCustomers(
            @RequestParam String month,
            @RequestParam String reasonCode
    ){
        return ResponseEntity.ok(customerSegmentAnalysisQueryService.getRiskReasonCustomers(month, reasonCode));
    }


    @Operation(
            summary = "월별 이탈 위험률 차트 조회",
            description = """
                    from~to 범위(YYYY-MM) 내 월별 이탈 위험률(월말 기준, %)을 반환합니다.
                    - 위험률(%) = (해당 월말 기준 이탈 위험 고객 수 / 해당 월말 기준 전체 고객 수) * 100
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "월별 위험률 조회 성공",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = MonthlyRiskRateResponseDTO.class)))),
                    @ApiResponse(responseCode = "400", description = "요청 파라미터 오류(형식 불일치/기간 역전 등)"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    @GetMapping("/risk-monthly-rate")
    public ResponseEntity<List<MonthlyRiskRateResponseDTO>> monthlyRate(
            @RequestParam("from") String fromMonth,
            @RequestParam("to") String toMonth
    ){
        return ResponseEntity.ok(customerSegmentAnalysisQueryService.getMonthlyRiskRate(fromMonth, toMonth));
    }


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
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CustomerSegmentTradeChartDTO.class)))
            ),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 형식 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/segmentTradeChart")
    public ResponseEntity<List<CustomerSegmentTradeChartDTO>> getSegmentTradeChart(
            @RequestParam(required = false) String month
    ){
        return ResponseEntity.ok(customerSegmentAnalysisQueryService.getSegmentTradeChart(month));
    }

    @Operation(
            summary = "세그먼트 상세 카드 조회",
            description = """
                선택한 세그먼트(segmentId)에 대한 상세 요약 카드 정보를 조회합니다.
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
    @GetMapping("/segmentCard")
    public ResponseEntity<CustomerSegmentDetailCardDTO> getCustomerSegmentDetailCard(
            @RequestParam Long segmentId
    ){
        return ResponseEntity.ok(customerSegmentAnalysisQueryService.getSegmentDetailCard(segmentId));
    }

    @GetMapping("/risk-customers")
    public ResponseEntity<CustomerSegmentRiskCustomerPageDTO> getRiskCustomers(
            @RequestParam String month,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(customerSegmentAnalysisQueryService.getRiskCustomersByMonth(month, page, size));
    }
}
