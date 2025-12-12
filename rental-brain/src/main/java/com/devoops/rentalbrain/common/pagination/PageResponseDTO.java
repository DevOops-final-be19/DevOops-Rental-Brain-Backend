package com.devoops.rentalbrain.common.pagination;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageResponseDTO<T> {

    // 실제 데이터 리스트
    private List<T> contents;

    // 전체 데이터 개수
    private long totalCount;

    // 페이지 버튼 정보 (현재 페이지, 시작/끝 페이지 번호 등)
    private PagingButtonInfo paging;
}