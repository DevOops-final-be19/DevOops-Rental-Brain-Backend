package com.devoops.rentalbrain.approval.command.Controller;

import com.devoops.rentalbrain.approval.command.dto.ApprovalRejectRequest;
import com.devoops.rentalbrain.approval.command.service.ApprovalCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/approval")
@Tag(name = "결제 관리(Command)", description = "계약 승인 처리 API")
public class ApprovalCommandController {
    private final ApprovalCommandService approvalCommandService;

    @Autowired
    public ApprovalCommandController(ApprovalCommandService approvalCommandService) {
        this.approvalCommandService = approvalCommandService;
    }

    @Operation(
            summary = "승인 처리",
            description = "지정된 승인 ID에 대해 승인 처리를 수행합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "승인 처리 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "이미 처리된 승인 요청",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "승인 요청을 찾을 수 없음",
                    content = @Content
            )
    })
    @PatchMapping("/approve/{approvalMappingId}")
    public ResponseEntity<Void> approve(
            @PathVariable Long approvalMappingId
    ) {
        approvalCommandService.approve(approvalMappingId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "반려 처리",
            description = "지정된 승인 ID에 대해 반려 처리 및 반려 사유를 저장합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "반려 처리 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "이미 처리된 승인 요청",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "승인 요청을 찾을 수 없음",
                    content = @Content
            )
    })
    @PatchMapping("/reject/{approvalMappingId}")
    public ResponseEntity<Void> reject(
            @PathVariable Long approvalMappingId,
            @RequestBody ApprovalRejectRequest request
    ) {
        approvalCommandService.reject(approvalMappingId, request.getRejectReason());
        return ResponseEntity.noContent().build();
    }
}
