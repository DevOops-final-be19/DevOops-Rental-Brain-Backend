package com.devoops.rentalbrain.common.notice.application.strategy;

import com.devoops.rentalbrain.common.notice.application.factory.NoticeMessageFactory;
import com.devoops.rentalbrain.common.notice.application.strategy.event.CustomerRegistEvent;
import com.devoops.rentalbrain.common.notice.command.service.NoticeCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class CustomerRegistStrategy {
    private final NoticeCommandService noticeCommandService;
    private final NoticeMessageFactory noticeMessageFactory;

    public CustomerRegistStrategy(NoticeCommandService noticeCommandService,
                                  NoticeMessageFactory noticeMessageFactory) {
        this.noticeCommandService = noticeCommandService;
        this.noticeMessageFactory = noticeMessageFactory;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(CustomerRegistEvent customerRegistEvent){
        log.info("CustomerRegistStrategy EventListener 호출");
        noticeCommandService.noticeAllCreate(
                noticeMessageFactory.customerRegistCreate(customerRegistEvent),
                customerRegistEvent.positionId()
        );
    }

}
