package com.devoops.rentalbrain.common.notice.application.strategy.event;

public record ApprovalRequestEvent(
        Long empId,
        String approvalName
) implements NotificationEvent{}
