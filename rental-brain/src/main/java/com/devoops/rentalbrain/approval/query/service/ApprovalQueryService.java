package com.devoops.rentalbrain.approval.query.service;

import com.devoops.rentalbrain.approval.query.dto.ApprovalStatusDTO;
import com.devoops.rentalbrain.approval.query.dto.PendingApprovalDTO;
import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;

public interface ApprovalQueryService {
    ApprovalStatusDTO getApprovalStatus(Long empId);
    PageResponseDTO<PendingApprovalDTO> getPendingApprovals(Long empId, Criteria criteria);
}
