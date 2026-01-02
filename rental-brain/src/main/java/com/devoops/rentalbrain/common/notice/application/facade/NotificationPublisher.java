package com.devoops.rentalbrain.common.notice.application.facade;

import com.devoops.rentalbrain.common.notice.application.strategy.event.NotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public NotificationPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // 알림 호출 인입점 인터페이스(facade)
    public void publish(NotificationEvent notificationEvent) {
        log.info("NotificationPublisher 호출");
        applicationEventPublisher.publishEvent(notificationEvent);
    }
}
