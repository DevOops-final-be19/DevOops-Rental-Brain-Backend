package com.devoops.rentalbrain.customer.customersegmenthistory.query.controller;

import com.devoops.rentalbrain.customer.customersegmenthistory.query.dto.RiskTransitionCountDTO;
import com.devoops.rentalbrain.customer.customersegmenthistory.query.dto.RiskTransitionHistoryDTO;
import com.devoops.rentalbrain.customer.customersegmenthistory.query.service.RiskTransitionQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/riskhistory")
@Tag(
        name = "고객 분석 - 위험 전이 이력",
        description = "고객의 이탈 위험/블랙리스트 상태 전이 이력을 조회합니다."
)
public class RiskTransitionQueryController {

    private final RiskTransitionQueryService riskTransitionQueryService;


    @Autowired
    public RiskTransitionQueryController(RiskTransitionQueryService riskTransitionQueryService) {
        this.riskTransitionQueryService = riskTransitionQueryService;
    }

    @Operation(
            summary = "이탈 위험 변경 이력",
            description = "고객의 이탈 위험 상태 변경 이력을 조회합니다.",
            responses = @ApiResponse(responseCode = "200", description = "조회 성공")
    )
    @GetMapping("/churn/{customerId}")
    public ResponseEntity<List<RiskTransitionHistoryDTO>> getChurnRiskTransitions(@PathVariable Long customerId) {
        return ResponseEntity.ok(riskTransitionQueryService.getChurnRiskTransitions(customerId));
    }


    @Operation(
            summary = "블랙리스트 변경 이력",
            description = "고객의 블랙리스트 상태 변경 이력을 조회합니다.",
            responses = @ApiResponse(responseCode = "200", description = "조회 성공")
    )
    @GetMapping("/blacklist/{customerId}")
    public ResponseEntity<List<RiskTransitionHistoryDTO>> getBlacklistTransitions(@PathVariable Long customerId) {
        return ResponseEntity.ok(riskTransitionQueryService.getBlacklistTransitions(customerId));
    }


    @Operation(
            summary = "위험 변경 횟수 요약",
            description = "고객의 이탈 위험/블랙리스트 전이 횟수를 한 번에 조회합니다.",
            responses = @ApiResponse(responseCode = "200", description = "조회 성공")
    )
    @GetMapping("/riskcount/{customerId}")
    public ResponseEntity<RiskTransitionCountDTO> getRiskTransitionCount(@PathVariable Long customerId) {
        return ResponseEntity.ok(riskTransitionQueryService.getRiskTransitionCount(customerId));
    }
}
