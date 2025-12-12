package com.devoops.rentalbrain.common.pagination;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

@Slf4j
public class Pagination {
    public static PagingButtonInfo getPagingButtonInfo(Page page) {
        int currentPage = page.getNumber() + 1;   // 인덱스 개념 -> 실제 페이지에 보여질 번호의 개념으로 다시 변경
        int defaultButtonCount = 10;              // 한 페이지에 보일 버튼의 갯수
        int startPage;                            // 한 페이지에 보여질 첫 버튼
        int endPage;                              // 한 페이지에 보여질 마지막 버튼

        log.debug("Pagination에서 currentPage 정보 확인: {}", currentPage);

        // 1~10, 11~20, 21~30 ... 이런 식으로 끊기기 위한 계산
        startPage = ((int)(currentPage / (double)defaultButtonCount + 0.9) - 1)
                * defaultButtonCount + 1;
        log.debug("Pagination에서 startPage 정보 확인: {}", startPage);
        endPage = startPage + defaultButtonCount - 1;


        // 전체 페이지 수보다 endPage가 크면 totalPages
        if(page.getTotalPages() < endPage) {     // totalPage가 마지막 페이지보다 작으면
            endPage = page.getTotalPages();      // totalPage가 마지막 페이지 버튼이 된다.
        }

        // 전체 페이지가 0인 경우(데이터 없음) → startPage = endPage
        if(page.getTotalPages() == 0) {          // 1페이지도 안된다면
            endPage = startPage;                 // startPage가 곧 endPage가 된다.(1페이지 버튼만 표시)
        }

        return new PagingButtonInfo(currentPage, startPage, endPage);
    }

    // MyBatis용 (추가)
    public static PagingButtonInfo getPagingButtonInfo(com.devoops.rentalbrain.common.pagination.Criteria criteria,
                                                       long totalCount) {

        int currentPage = criteria.getPageNum();
        int amount = criteria.getAmount();
        int defaultButtonCount = 10;

        // 전체 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalCount / amount);

        int startPage = ((int)(currentPage / (double)defaultButtonCount + 0.9) - 1)
                * defaultButtonCount + 1;
        int endPage = startPage + defaultButtonCount - 1;

        if (totalPages < endPage) {
            endPage = totalPages;
        }

        if (totalPages == 0) {
            endPage = startPage;
        }

        return new PagingButtonInfo(currentPage, startPage, endPage);
    }
}
