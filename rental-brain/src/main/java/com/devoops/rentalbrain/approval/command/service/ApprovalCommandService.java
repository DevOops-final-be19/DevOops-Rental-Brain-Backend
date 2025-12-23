package com.devoops.rentalbrain.approval.command.service;

public interface ApprovalCommandService {
    void approve();
    void reject(String rejectReason);
}
