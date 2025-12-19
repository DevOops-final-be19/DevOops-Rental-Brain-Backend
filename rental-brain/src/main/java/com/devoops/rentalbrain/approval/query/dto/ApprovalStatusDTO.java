package com.devoops.rentalbrain.approval.query.dto;

import lombok.Data;

@Data
public class ApprovalStatusDTO {
    private Long my_approved;
    private Long my_rejected;
    private Long my_pending;
    private Long fully_approved_count;
    private Long not_fully_approved_count;
}
