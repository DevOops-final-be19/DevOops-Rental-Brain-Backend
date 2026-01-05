package com.devoops.rentalbrain.common.notice.application.strategy.event;

public record ContractApprovedEvent(
        Long EmpId,
        Character isApproved
) implements NotificationEvent {}
