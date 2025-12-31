package com.devoops.rentalbrain.approval.query.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PendingApprovalDTO {
    // approval_mapping
    private Long approvalMappingId;
    private Long approverEmpId;
    private Integer approvalStep;
    private String isApproved;

    // approval
    private String approvalCode;
    private String approvalTitle;
    private Long contractId;
    private LocalDateTime requestDate;
    private Long requestEmpId;

    // employee
    private String employeeCode;
    private String employeeName;

    // position
    private String positionName;
}
