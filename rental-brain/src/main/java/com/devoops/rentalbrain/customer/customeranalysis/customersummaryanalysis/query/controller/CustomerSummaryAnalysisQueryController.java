package com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.controller;

import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.dto.*;
import com.devoops.rentalbrain.customer.customeranalysis.customersummaryanalysis.query.service.CustomerSummaryAnalysisQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/customerSummaryAnalysis")
@Slf4j
@Tag(
        name = "고객 요약 분석(Query)",
        description = "고객 요약 및 세그먼트 분포 등 분석용 조회 API"
)
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
                    - 총 고객 수
                    - 평균 거래액(월)
                    - 평균 만족도(월)
                    
                    ※ 이탈 위험 KPI는 /customersegmentanalysis/riskKpi API에서 제공합니다.
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


    // 만족도 상세 조회(별점별 회사 목록) - 고객별 '최근 피드백 1건' 기준
    @Operation(
            summary = "만족도(별점) 상세 - 별점별 회사 목록 조회(페이징)",
            description = """
                star(1~5)에 해당하는 회사(고객) 목록을 조회합니다.
                기준: 고객별 가장 최근 피드백 1건을 대표로 사용합니다.
                페이징: quote와 동일하게 page/size → Criteria 사용
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/satisfaction/{star}/customers")
    public ResponseEntity<PageResponseDTO<CustomerSummaryAnalysisQuerySatisfactionCustomerDTO>> satisfactionCustomers(
            @PathVariable int star,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Criteria criteria = new Criteria(page, size);
        return ResponseEntity.ok(customerSummaryAnalysisQueryService.getCustomersByStarWithPaging(star, criteria));
    }

    @Operation(
            summary = "고객 세그먼트 분포 조회",
            description = """
        현재 기준 고객 세그먼트 분포를 조회합니다.
        
        - 기준: customer 테이블의 현재 segment_id
        - 대상: 삭제되지 않은 고객(is_deleted = 'N')
        - 용도: 고객 세그먼트 분석 파이 차트
        
        ※ 월별/기간별이 아닌 '현재 상태 스냅샷' 데이터입니다.
        """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "고객 세그먼트 분포 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/segment-distribution")
    public CustomerSegmentDistributionResponseDTO getSegmentDistribution() {
        return customerSummaryAnalysisQueryService.getSegmentDistribution();
    }

}
