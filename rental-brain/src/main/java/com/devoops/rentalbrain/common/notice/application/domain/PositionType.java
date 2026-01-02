package com.devoops.rentalbrain.common.notice.application.domain;

public enum PositionType {
    CEO(1L),
    CUSTOMER_MANAGER(2L),
    BUSINESS_MANAGER(3L),
    PRODUCT_MANAGER(4L),
    CUSTOMER(5L),
    BUSINESS(6L),
    PRODUCT(7L);

    private final Long positionNum;

    PositionType(Long positionNum) {
        this.positionNum = positionNum;
    }

    public Long positionNum() {
        return positionNum;
    }

}
