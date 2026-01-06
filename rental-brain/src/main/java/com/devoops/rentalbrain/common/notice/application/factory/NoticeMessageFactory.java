package com.devoops.rentalbrain.common.notice.application.factory;

import com.devoops.rentalbrain.common.notice.application.strategy.event.*;
import com.devoops.rentalbrain.common.notice.command.entity.Notification;
import org.springframework.stereotype.Component;

// 알림 생성
@Component
public class NoticeMessageFactory {

    public NoticeMessageFactory() {}

    public Notification ApprovalRequestCreate(ApprovalRequestEvent approvalRequestEvent){
        return new Notification(
                "APPROVAL_REQUEST",
                "결재 대기중",
                "대기중인 결재가 있습니다: " + approvalRequestEvent.approvalName(),
                "/approval/"
        );
    }

    public Notification customerRegistCreate(CustomerRegistEvent customerRegistEvent) {
        return new Notification(
                "CUSTOMER_REGIST",
                "신규 고객 등록",
                "신규 고객이 등록되었습니다: " + customerRegistEvent.company(),
                "/customer/" + customerRegistEvent.cmpId()
        );
    }

    public Notification productRegistCreate(ProductRegistEvent productRegistEvent) {
        return new Notification(
                "PRODUCT_REGIST",
                "신규 자산 등록",
                "신규 자산이 등록되었습니다: " + productRegistEvent.Item(),
                "/product/" + productRegistEvent.itemId()
        );
    }

    public Notification contractApprovedCreate(ContractApprovedEvent contractApprovedEvent) {
        if(contractApprovedEvent.isApproved()=='A'){
            return new Notification(
                    "APPROVAL",
                    "결재 승인",
                    "등록한 결재가 승인되었습니다.",
                    "/contract/"
            );
        }
        else{
            return new Notification(
                    "REJECT",
                    "결재 반려",
                    "등록한 결재가 반려되었습니다.",
                    "/contract/"
            );
        }
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
