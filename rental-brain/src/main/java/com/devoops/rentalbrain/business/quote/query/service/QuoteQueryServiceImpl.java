package com.devoops.rentalbrain.business.quote.query.service;

import com.devoops.rentalbrain.business.quote.query.dto.QuoteDetailQueryResponseDTO;
import com.devoops.rentalbrain.business.quote.query.dto.QuoteKpiResponseDTO;
import com.devoops.rentalbrain.business.quote.query.dto.QuoteQueryResponseDTO;
import com.devoops.rentalbrain.business.quote.query.mapper.QuoteQueryMapper;
import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.common.pagination.Pagination;
import com.devoops.rentalbrain.common.pagination.PagingButtonInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor  // 생성자 자동으로 해주는 어노테이션
public class QuoteQueryServiceImpl implements QuoteQueryService {
    private final QuoteQueryMapper quoteQueryMapper;


    // 페이징 없는 전체 조회
    @Override
    public List<QuoteQueryResponseDTO> getQuoteList(
            String customerName,
            String customerInCharge,
            String customerCallNum,
            Integer quoteChannelId,
            String quoteCounselor
    ) {
        return quoteQueryMapper.getQuoteList(
                customerName,
                customerInCharge,
                customerCallNum,
                quoteChannelId,
                quoteCounselor
        );
    }

    // 페이징 조회
    @Override
    public List<QuoteQueryResponseDTO> getQuoteListWithPaging(
            String customerName,
            String customerInCharge,
            String customerCallNum,
            Integer quoteChannelId,
            String quoteCounselor,
            int offset,
            int limit) {
        return quoteQueryMapper.getQuoteListWithPaging(
                customerName,
                customerInCharge,
                customerCallNum,
                quoteChannelId,
                quoteCounselor,
                offset,
                limit
        );

    }


    // 페이지네이션 + 검색 + 공통 응답
    @Override
    public PageResponseDTO<QuoteQueryResponseDTO> getQuoteListWithPaging(
            String customerName,
            String customerInCharge,
            String customerCallNum,
            Integer quoteChannelId,
            String quoteCounselor,
            Criteria criteria
    ) {

        // 데이터 목록 조회
        List<QuoteQueryResponseDTO> contents =
                quoteQueryMapper.getQuoteListWithPaging(
                        customerName,
                        customerInCharge,
                        customerCallNum,
                        quoteChannelId,
                        quoteCounselor,
                        criteria.getOffset(),
                        criteria.getAmount()
                );

        //  전체 건수 조회
        long totalCount = quoteQueryMapper.countQuoteList(
                customerName,
                customerInCharge,
                customerCallNum,
                quoteChannelId,
                quoteCounselor
        );

        //  페이지 버튼 정보 계산 (MyBatis용 유틸 사용)
        PagingButtonInfo paging =
                Pagination.getPagingButtonInfo(criteria, totalCount);

        //  공통 페이지 응답으로 감싸서 반환
        return new PageResponseDTO<>(contents, totalCount, paging);
    }


    // 견적/상담 디테일 조회
    @Override
    public QuoteDetailQueryResponseDTO getQuoteDetail(Long quoteId) {
        return quoteQueryMapper.getQuoteDetail(quoteId);
    }

    // KPI 조회
    @Override
    public QuoteKpiResponseDTO getQuoteKpi() {
        return quoteQueryMapper.getQuoteKpi();
    }
}