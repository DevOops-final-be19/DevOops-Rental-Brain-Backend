package com.devoops.rentalbrain.common.notice.application.factory;

import com.devoops.rentalbrain.common.notice.application.strategy.event.ContractApprovedEvent;
import com.devoops.rentalbrain.common.notice.application.strategy.event.QuoteInsertedEvent;
import com.devoops.rentalbrain.common.notice.command.entity.Notification;
import org.springframework.stereotype.Component;

// 알림 생성
@Component
public class NoticeMessageFactory {

    public NoticeMessageFactory() {}

    public Notification contractApprovedCreate(ContractApprovedEvent contractApprovedEvent) {
        return new Notification(
                "QUOTE_INSERT",
                "계약 승인",
                "등록한 계약이 승인되었습니다.",
                "/contract/"
        );
    }

    public Notification quoteInsertedCreate(QuoteInsertedEvent quoteInsertedEvent) {
        return new Notification(
                "QUOTE_INSERT",
                "견적 상담 등록",
                quoteInsertedEvent.company() + "에서 신규 견적 상담이 등록되었습니다.",
                "/quote/" + quoteInsertedEvent.cmpId()
        );
    }
}
