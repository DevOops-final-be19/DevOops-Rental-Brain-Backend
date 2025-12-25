package com.devoops.rentalbrain.approval.command.service;

public interface ApprovalCommandService {
    void approve(Long approvalMappingId);
    void reject(Long approvalMappingId, String rejectReason);
}
