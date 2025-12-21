package com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.controller;

import com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.dto.ChurnKpiCardResponseDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.dto.CustomerSummaryAnalysisQueryKPIDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.dto.CustomerSummaryAnalysisQuerySatisfactionDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.dto.MonthlyRiskRateResponseDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.service.CustomerSummaryAnalysisQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/customerSummaryAnalysis")
@Slf4j
public class CustomerSummaryAnalysisQueryController {

    private final CustomerSummaryAnalysisQueryService customerSummaryAnalysisQueryService;

    @Autowired
    public CustomerSummaryAnalysisQueryController(CustomerSummaryAnalysisQueryService customerSummaryAnalysisQueryService) {
        this.customerSummaryAnalysisQueryService = customerSummaryAnalysisQueryService;
    }


    @GetMapping("/health")
    public String health() {
        return "CustomerSummaryAnalysis OK";
    }

    // 고객 요약 분석 KPI(5개 카드 통합)
    @Operation(
            summary = "고객 요약 분석 KPI(5개 카드) 조회",
            description = """
                    입력한 기준월(YYYY-MM)을 기반으로 고객 요약 KPI를 반환합니다.
                    - 총 고객 수(누적) + 전월 대비(월 활동 고객 기준 %)
                    - 평균 거래액(월) = monthly_payment 기반 고객당 평균 월 과금액 + 전월 대비(%)
                    - 평균 만족도(월) + 전월 대비(점)
                    - 안정 고객 비율(%) + 안정 고객 수
                    - 이탈 위험률(%) + 이탈 위험 고객 수 + 전월 대비(%p)
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "KPI 조회 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerSummaryAnalysisQueryKPIDTO.class))),
                    @ApiResponse(responseCode = "400", description = "요청 파라미터 오류"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    @GetMapping("/kpi")
    public ResponseEntity<CustomerSummaryAnalysisQueryKPIDTO> kpi(
            @RequestParam("month") String month
    ) {
        return ResponseEntity.ok(customerSummaryAnalysisQueryService.getkpi(month));
    }



    // 이탈률 Kpi 카드 조회
    @Operation(
            summary = "이탈 위험 KPI 카드 조회",
            description = """
                    입력한 기준월(YYYY-MM)을 기반으로 KPI 카드 데이터를 반환합니다.
                    - 전체 고객 수
                    - 해당 월 이탈 위험 고객 수 / 위험률(%)
                    - 전월 이탈 위험 고객 수 / 위험률(%)
                    - 전월 대비 위험률 변화(MoM, %p)
                    - 유지 고객 수 / 유지율(%)
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "KPI 카드 조회 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ChurnKpiCardResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "요청 파라미터 오류(형식 불일치 등)"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    @GetMapping("/riskKpi")
    public ResponseEntity<ChurnKpiCardResponseDTO> kpiCard(@RequestParam("month") String month) {

        ChurnKpiCardResponseDTO kpiCard = customerSummaryAnalysisQueryService.getRiskKpi(month);

        return ResponseEntity.ok(kpiCard);
    }

    // 차트 (월별 위험률 차트)
    @Operation(
            summary = "월별 이탈 위험률 차트 조회",
            description = """
                    from~to 범위(YYYY-MM) 내 월별 이탈 위험률(%)을 반환합니다.
                    차트 X축: 월(YYYY-MM)
                    차트 Y축: 위험률(%)
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "월별 위험률 조회 성공",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = MonthlyRiskRateResponseDTO.class)))),
                    @ApiResponse(responseCode = "400", description = "요청 파라미터 오류(형식 불일치/기간 역전 등)"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    @GetMapping("/risk-monthly-rate")
    public ResponseEntity<List<MonthlyRiskRateResponseDTO>> monthlyRate(
            @RequestParam("from") String fromMonth,
            @RequestParam("to") String toMonth
    ){
        List<MonthlyRiskRateResponseDTO> list
                = customerSummaryAnalysisQueryService.getMonthlyRiskRate(fromMonth, toMonth);

        return ResponseEntity.ok(list);
    }

    // 차트 만족도 분포
    @GetMapping("/satisfaction")
    @Operation(
            summary = "고객 만족도 분포 조회",
            description = "고객 만족도(1~5점) 분포를 조회합니다. (전체 기준)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    public CustomerSummaryAnalysisQuerySatisfactionDTO getSatisfaction() {
        return customerSummaryAnalysisQueryService.getSatisfaction();
    }


}
