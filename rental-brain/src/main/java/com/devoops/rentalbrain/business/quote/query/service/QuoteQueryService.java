package com.devoops.rentalbrain.business.quote.query.service;

import com.devoops.rentalbrain.business.quote.query.dto.QuoteDetailQueryResponseDTO;
import com.devoops.rentalbrain.business.quote.query.dto.QuoteKpiResponseDTO;
import com.devoops.rentalbrain.business.quote.query.dto.QuoteQueryResponseDTO;
import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;

import java.util.List;

public interface QuoteQueryService {
    // 페이징 없는 전체 조회
    List<QuoteQueryResponseDTO> getQuoteList(
            String customerName,
            String customerInCharge,
            String customerCallNum,
            Integer quoteChannelId,
            String quoteCounselor
    );

    // 페이징 조회
    List<QuoteQueryResponseDTO> getQuoteListWithPaging(
            String customerName,
            String customerInCharge,
            String customerCallNum,
            Integer quoteChannelId,
            String quoteCounselor,
            int offset,
            int limit);

    // 페이지네이션 + 검색 + 공통 응답
    PageResponseDTO<QuoteQueryResponseDTO> getQuoteListWithPaging(
            String customerName,
            String customerInCharge,
            String customerCallNum,
            Integer quoteChannelId,
            String quoteCounselor,
            Criteria criteria
    );

    // 견적/상담 디테일 조회
    QuoteDetailQueryResponseDTO getQuoteDetail(Long quoteId);

    // KPI 조회
    QuoteKpiResponseDTO getQuoteKpi();
}
