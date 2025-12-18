package com.devoops.rentalbrain.customer.customersegmenthistory.command.domain;

public enum SegmentChangeReferenceType {
    /*
    * LOW_SAT → FEEDBACK
    * EXPIRING → CONTRACT
    * OVERDUE → PAYMENT
    * 캠페인 복귀 → CAMPAIGN
    * */

    FEEDBACK,      // 고객 피드백 feedback.id
    CONTRACT,      // 계약 정보 contract.id
    PAYMENT,       // 결제/연체 정보 contract.id
    CAMPAIGN,      // 캠페인/쿠폰 promotion_log.id
    SYSTEM_RULE,   // 내부 룰 (ex. 3개월 조건)
    NONE           // 명시적 근거 없음
}
