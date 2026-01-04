package com.devoops.rentalbrain.common.notice.application.strategy.event;

import com.devoops.rentalbrain.common.notice.application.domain.PositionType;

import java.util.List;

public record ProductRegistEvent(
        List<PositionType> positionId,
        String Item,
        Long itemId
) implements NotificationEvent{}
