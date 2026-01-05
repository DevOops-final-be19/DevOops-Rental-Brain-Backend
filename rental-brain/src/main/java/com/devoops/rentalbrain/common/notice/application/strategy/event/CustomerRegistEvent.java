package com.devoops.rentalbrain.common.notice.application.strategy.event;

import com.devoops.rentalbrain.common.notice.application.domain.PositionType;

import java.util.List;

public record CustomerRegistEvent(
        List<PositionType> positionId,
        String company,
        Long cmpId
) implements NotificationEvent {}
