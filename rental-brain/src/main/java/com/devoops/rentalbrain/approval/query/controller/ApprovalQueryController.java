package com.devoops.rentalbrain.approval.query.controller;

import com.devoops.rentalbrain.approval.query.dto.ApprovalStatusDTO;
import com.devoops.rentalbrain.approval.query.dto.PendingApprovalDTO;
import com.devoops.rentalbrain.approval.query.service.ApprovalQueryService;
import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/approval")
public class ApprovalQueryController {

    private ApprovalQueryService approvalQueryService;

    @Autowired
    public ApprovalQueryController(ApprovalQueryService approvalQueryService) {
        this.approvalQueryService = approvalQueryService;
    }

    @GetMapping("/status/{empId}")
    public ResponseEntity<ApprovalStatusDTO> getApprovalStatus(
            @PathVariable Long empId
    ){
        return ResponseEntity.ok(approvalQueryService.getApprovalStatus(empId));
    }

    @GetMapping("/pending/{empId}")
    public PageResponseDTO<PendingApprovalDTO> getPendingApprovals(
            @PathVariable Long empId,
            Criteria criteria
    ) {
        return approvalQueryService.getPendingApprovals(empId, criteria);
    }
}
