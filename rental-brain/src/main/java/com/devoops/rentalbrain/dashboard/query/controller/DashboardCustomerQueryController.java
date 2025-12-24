package com.devoops.rentalbrain.dashboard.query.controller;

import com.devoops.rentalbrain.dashboard.query.dto.DashboardKpiResponseDTO;
import com.devoops.rentalbrain.dashboard.query.dto.QuarterlyCustomerTrendResponseDTO;
import com.devoops.rentalbrain.dashboard.query.service.DashboardCustomerQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "대시보드(Query)",
        description = "로그인 대시보드에서 사용하는 트렌드/현황 조회 API"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardCustomerQueryController {

    private final DashboardCustomerQueryService service;

    @Operation(
            summary = "대시보드 KPI 4종 조회",
            description = """
                    로그인 대시보드 KPI 4종을 조회합니다.
                    1) 만료 임박 계약(D-60): (계약종료일 - 60일) 이후이며, 진행/만료임박 상태(P/I) 계약 수
                    2) 납부 연체: 진행중(P) 연체 건수
                    3) 문의 대기: 대기(P) 문의 건수
                    4) 이번 달 매출(MTD): 해당 월 계약 매출 합 + 전월 대비(MoM)
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            content = @Content(schema = @Schema(implementation = DashboardKpiResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    @GetMapping("/kpi")
    public ResponseEntity<DashboardKpiResponseDTO> kpi(
            @Parameter(description = "기준 월(YYYY-MM), 미지정 시 이번 달", example = "2025-12", required = false)
            @RequestParam(required = false) String month
    ) {
        return ResponseEntity.ok(service.getDashboardKpi(month));
    }


    @Operation(
            summary = "분기별 고객 수 트렌드 조회",
            description = """
                    해당 연도의 분기별 고객 트렌드를 조회합니다.
                    - 전체 고객 수: 분기말 기준 '첫 계약일'이 분기 종료일 이전(<=)인 고객 수(누적)
                    - 신규 고객 수: 분기 내 '첫 계약일'이 분기 시작~종료 사이인 고객 수
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            content = @Content(schema = @Schema(implementation = QuarterlyCustomerTrendResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    @GetMapping("/quarterly")
    public ResponseEntity<QuarterlyCustomerTrendResponseDTO> quarterly(
            @Parameter(description = "조회 연도(미지정 시 현재 연도)", example = "2025")
            @RequestParam(required = false) Integer year
    ) {
        return ResponseEntity.ok(service.getQuarterlyTrend(year));
    }


}
