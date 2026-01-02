package com.devoops.rentalbrain.approval.query.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApprovalCompletedDTO {

    /** 결재 계약 아이디 */
    private long contractId;

    /** 승인 코드 (APP-2025-001) */
    private String approvalCode;

    /** 승인 제목 */
    private String approvalTitle;

    /** 승인 요청일 */
    private LocalDateTime requestDate;

    /** 마지막 처리일 (최종 승인일 or 반려일) */
    private LocalDateTime lastProcessDate;

    /** 거절사유 */
    private String rejectReason;

    /** 결과 상태 (APPROVED / REJECTED) */
    private String resultStatus;


}
