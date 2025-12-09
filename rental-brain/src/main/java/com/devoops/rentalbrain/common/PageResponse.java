package com.devoops.rentalbrain.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class PageResponse<T> {
    private List<T> list;       // 데이터 목록
    private int totalCount;     // 전체 개수
    private boolean hasNext;    // 다음 페이지 여부

    public PageResponse(List<T> list, int totalCount, PageRequest pageRequest) {
        this.list = list;
        this.totalCount = totalCount;
        // (현재 페이지 * 사이즈) < 전체 개수라면 다음 페이지 존재
        this.hasNext = (pageRequest.getPage() * pageRequest.getSize()) < totalCount;
    }
}