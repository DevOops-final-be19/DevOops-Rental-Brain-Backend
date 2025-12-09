package com.devoops.rentalbrain.common;

import lombok.Data;

@Data
public class PageRequest {
    private int page = 1;  // 기본 1페이지
    private int size = 10; // 기본 10개씩

    // MyBatis에서 사용할 건너뛰기 개수(Offset) 자동 계산
    public int getOffset() {
        return (Math.max(1, page) - 1) * size;
    }
}