package com.devoops.rentalbrain.common.pagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Criteria {
    private int pageNum;  // 현재 페이지
    private int amount;   // 페이지당 보여줄 개수

    private String type;    // 검색 유형
    private String keyword; // 검색어

    public Criteria() {
        this(1, 10);
    }

    public Criteria(int pageNum, int amount) {
        this.pageNum = pageNum;
        this.amount = amount;
    }

    // MyBatis 쿼리에서 #{offset}으로 사용하기 위함
    public int getOffset() {
        return (pageNum - 1) * amount;
    }
}