package com.devoops.rentalbrain.common.codegenerator;

/**
 * 비즈니스 코드 유형 정의 Enum
 *
 * 규칙:
 * - prefix: 테이블명 3글자 축약
 * - 실제 코드 형식: PREFIX-YYYY-NNN
 *
 * 예:
 *  - CUS-2025-001 (고객)
 *  - EMP-2025-003 (사원)
 *  - CON-2025-010 (계약)
 */
public enum CodeType {

    CUSTOMER("CUS"),
    EMPLOYEE("EMP"),
    CONTRACT("CON"),
    QUOTE("QUO"),
    COUPON("COU"),
    PROMOTION("PRO"),
    SURVEY("SUR"),
    ITEM("ITE"),
    AFTER_SERVICE("AFT"),
    FEEDBACK("FDB"),
    CUSTOMER_SUPPORT("CSU"),
    APPROVAL("APP"),
    ITEM_OVERDUE("IOD"),
    PAY_OVERDUE("POD");

    private final String prefix;

    CodeType(String prefix) {
        this.prefix = prefix;
    }

    public String prefix() {
        return prefix;
    }
}