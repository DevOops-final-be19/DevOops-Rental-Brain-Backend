package com.devoops.rentalbrain.approval.query.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApprovalProgressDTO {
    private String approval_code;
    private String approval_title;
    private LocalDateTime requestDate;

    private int totalStep;
    private int approvedStep;
    private int progressRate;

    // 다음 승인자
    private String nextApproverName;
    private String nextApproverPosition;
}
