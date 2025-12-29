package com.devoops.rentalbrain.customer.customersegmenthistory.command.domain;

public enum SegmentChangeTriggerType {

    AUTO,
    CHURN_RISK,    // 이탈 위험 판별 로직에 의해 자동 변경
    SYSTEM,        // 시스템 정책에 의한 변경 (배치 안쓸꺼임, 룰 엔진)
    MANUAL,        // 관리자 수동 변경
    CAMPAIGN,      // 캠페인/프로모션/쿠폰 영향
    BATCH,
    CONTRACT       // 계약 생성/종료로 인한 변경
}
