package com.devoops.rentalbrain.approval.query.service;

import com.devoops.rentalbrain.approval.query.dto.ApprovalCompletedDTO;
import com.devoops.rentalbrain.approval.query.dto.ApprovalProgressDTO;
import com.devoops.rentalbrain.approval.query.dto.ApprovalStatusDTO;
import com.devoops.rentalbrain.approval.query.dto.PendingApprovalDTO;
import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;

public interface ApprovalQueryService {
    ApprovalStatusDTO getApprovalStatus();
    PageResponseDTO<PendingApprovalDTO> getPendingApprovals(Criteria criteria);
    PageResponseDTO<ApprovalProgressDTO> getApprovalProgress(Criteria criteria);
    PageResponseDTO<ApprovalCompletedDTO> getApprovalCompleted(Criteria criteria);
}
