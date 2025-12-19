package com.devoops.rentalbrain.customer.customeranalysis.customerchurnsnapshot.query.controller;

import com.devoops.rentalbrain.customer.customeranalysis.customerchurnsnapshot.query.dto.ChurnKpiCardResponseDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customerchurnsnapshot.query.dto.MonthlyRiskRateResponseDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customerchurnsnapshot.query.service.CustomerChurnSnapshotQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customer-analysis/churn-snapshot")
@Tag(
        name = "고객 분석 - 이탈 위험 스냅샷(Query)",
        description = "이탈 위험 고객 기반 KPI 카드(전월 대비 포함) 및 월별 위험률 차트 데이터를 조회합니다."
)
public class CustomerChurnSnapshotQueryController {

    private final CustomerChurnSnapshotQueryService customerChurnSnapshotQueryService;


    @Autowired
    public CustomerChurnSnapshotQueryController(CustomerChurnSnapshotQueryService customerChurnSnapshotQueryService) {
        this.customerChurnSnapshotQueryService = customerChurnSnapshotQueryService;
    }

    @GetMapping("/health")
    public String health() {
        return "risk OK";
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
    @GetMapping("/kpi-card")
    public ResponseEntity<ChurnKpiCardResponseDTO> kpiCard(@RequestParam("month") String month) {

        ChurnKpiCardResponseDTO kpiCard = customerChurnSnapshotQueryService.getKpiCard(month);

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
    @GetMapping("/monthly-rate")
    public ResponseEntity<List<MonthlyRiskRateResponseDTO>> monthlyRate(
            @RequestParam("from") String fromMonth,
            @RequestParam("to") String toMonth
    ){
        List<MonthlyRiskRateResponseDTO> list
                = customerChurnSnapshotQueryService.getMonthlyRiskRate(fromMonth, toMonth);

        return ResponseEntity.ok(list);
    }


}
