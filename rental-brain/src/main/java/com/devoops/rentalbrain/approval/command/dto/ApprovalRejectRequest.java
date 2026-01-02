package com.devoops.rentalbrain.approval.command.dto;

import lombok.Data;

@Data
public class ApprovalRejectRequest {
    private String rejectReason;
}
