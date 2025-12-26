package com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.controller;


import com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.dto.CustomerSupportAnalysisMonthlyTrendResponseDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.dto.CustomerSupportAnalysisQueryResponseKPIDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.service.CustomerSupportAnalysisQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/customerSupportAnalysis")
@Slf4j
@Tag(
        name = "고객 응대 분석(Query)",
        description = "고객 상담·응대 데이터를 기반으로 KPI 분석 정보를 조회합니다."
)
public class CustomerSupportAnalysisQueryController {

    private final CustomerSupportAnalysisQueryService customerSupportAnalysisQueryService;

    @Autowired
    public CustomerSupportAnalysisQueryController(CustomerSupportAnalysisQueryService customerSupportAnalysisQueryService) {
        this.customerSupportAnalysisQueryService = customerSupportAnalysisQueryService;
    }


    // 고객 응대 분석 kpi
    @Operation(
            summary = "고객 응대 분석 KPI 조회",
            description = """
            선택한 월 기준으로 고객 응대 관련 KPI를 조회합니다.

            - 상담 완료율
            - 평균 응답 시간
            - 전월 대비 변화(MoM)
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "KPI 조회 성공"),
                    @ApiResponse(responseCode = "400", description = "요청 파라미터 오류"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @GetMapping("/kpi")
    public CustomerSupportAnalysisQueryResponseKPIDTO getKpi(
            @RequestParam String month
    ) {
        return customerSupportAnalysisQueryService.getKpi(month);
    }

    // ✅ 월별 응대량 트렌드 (월별 = 그 달만)
    @GetMapping("/monthly-trend")
    @Operation(
            summary = "월별 응대량 트렌드 조회",
            description = """
                    1~12월 월별 응대량(견적/문의/피드백/설문)을 조회합니다.
                    누적(YTD)이 아니라 '월별(그 달만)' 데이터입니다.
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "요청 파라미터 오류"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    public CustomerSupportAnalysisMonthlyTrendResponseDTO getMonthlyTrend(
            @RequestParam(required = false) Integer year
    ) {
        int targetYear = (year == null) ? LocalDate.now().getYear() : year;
        return customerSupportAnalysisQueryService.getMonthlyTrend(targetYear);
    }


}
