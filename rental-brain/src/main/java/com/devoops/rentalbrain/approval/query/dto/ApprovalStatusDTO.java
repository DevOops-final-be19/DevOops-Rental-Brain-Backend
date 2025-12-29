package com.devoops.rentalbrain.approval.query.dto;

import lombok.Data;

@Data
public class ApprovalStatusDTO {
    private Long my_pending;
    private Long total_approved;
    private Long total_rejected;
    private Long in_progress;
}
