package com.devoops.rentalbrain.common.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class PagingButtonInfo {

    // 현재 페이지 (실제 화면에 보이는 번호)
    private int currentPage;

    // 페이지 버튼 영역의 시작 번호
    private int startPage;

    // 페이지 버튼 영역의 끝 번호
    private int endPage;
}
