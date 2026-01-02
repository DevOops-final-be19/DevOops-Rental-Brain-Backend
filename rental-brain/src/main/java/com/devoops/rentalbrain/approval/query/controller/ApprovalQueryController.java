package com.devoops.rentalbrain.approval.query.controller;

import com.devoops.rentalbrain.approval.query.dto.ApprovalCompletedDTO;
import com.devoops.rentalbrain.approval.query.dto.ApprovalProgressDTO;
import com.devoops.rentalbrain.approval.query.dto.ApprovalStatusDTO;
import com.devoops.rentalbrain.approval.query.dto.PendingApprovalDTO;
import com.devoops.rentalbrain.approval.query.service.ApprovalQueryService;
import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/approval")
@Tag(name = "결재 조회(Query)", description = "결재/승인 조회 API")
public class ApprovalQueryController {

    private ApprovalQueryService approvalQueryService;

    @Autowired
    public ApprovalQueryController(ApprovalQueryService approvalQueryService) {
        this.approvalQueryService = approvalQueryService;
    }

    @Operation(
            summary = "개인 승인 상태 요약 조회",
            description = """
            특정 사원의 승인 상태를 요약 조회합니다.
            
            - 내가 승인한 건수
            - 내가 반려한 건수
            - 내가 대기 중인 건수
            - 전체 승인 진행 현황
            """
    )
    @ApiResponse(
            responseCode = "200",
            description = "승인 상태 조회 성공",
            content = @Content(schema = @Schema(implementation = ApprovalStatusDTO.class))
    )
    @GetMapping("/status")
    public ResponseEntity<ApprovalStatusDTO> getApprovalStatus(){
        return ResponseEntity.ok(approvalQueryService.getApprovalStatus());
    }

    @Operation(
            summary = "승인 대기 목록 조회",
            description = """
            현재 로그인한 사원이 승인해야 할 대기 목록을 조회합니다.
            
            - 아직 승인하지 않은 건
            - 이전 단계가 모두 승인 완료된 건만 노출
            """
    )
    @ApiResponse(
            responseCode = "200",
            description = "승인 대기 목록 조회 성공"
    )
    @GetMapping("/pending")
    public PageResponseDTO<PendingApprovalDTO> getPendingApprovals(
            @ModelAttribute Criteria criteria
    ) {
        return approvalQueryService.getPendingApprovals(criteria);
    }

    @Operation(
            summary = "승인 진행 목록 조회",
            description = """
            현재 진행 중인 승인 목록을 조회합니다.
            
            - 일부 단계가 승인됨
            - 최종 승인 전 상태
            - 진행률(%) 포함
            """
    )
    @ApiResponse(
            responseCode = "200",
            description = "승인 진행 목록 조회 성공"
    )
    @GetMapping("/progress")
    public PageResponseDTO<ApprovalProgressDTO> getApprovalProgress(
            @ModelAttribute Criteria criteria
    ) {
        return approvalQueryService.getApprovalProgress(criteria);
    }

    @Operation(
            summary = "승인 완료 목록 조회",
            description = """
            승인 완료된 결재 목록을 조회합니다.
            
            - 승인 완료
            - 반려 완료
            - 최종 처리 일자 포함
            """
    )
    @ApiResponse(
            responseCode = "200",
            description = "승인 완료 목록 조회 성공"
    )
    @GetMapping("/completed")
    public PageResponseDTO<ApprovalCompletedDTO> getApprovalCompleted(
            @ModelAttribute Criteria criteria
    ){
        return approvalQueryService.getApprovalCompleted(criteria);
    }
}
