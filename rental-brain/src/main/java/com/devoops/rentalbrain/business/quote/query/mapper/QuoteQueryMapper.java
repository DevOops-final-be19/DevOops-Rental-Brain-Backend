package com.devoops.rentalbrain.business.quote.query.mapper;

import com.devoops.rentalbrain.business.quote.query.dto.QuoteDetailQueryResponseDTO;
import com.devoops.rentalbrain.business.quote.query.dto.QuoteKpiResponseDTO;
import com.devoops.rentalbrain.business.quote.query.dto.QuoteQueryResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuoteQueryMapper {

    // 상단 kpi 카드  조회 (전체 상담, 오늘 상담, 평균 처리시간)
    QuoteKpiResponseDTO getQuoteKpi();


    // 전체조회(페이지네이션 X)
    List<QuoteQueryResponseDTO> getQuoteList(
                                             @Param("customerName") String customerName,
                                             @Param("customerInCharge") String customerInCharge,
                                             @Param("customerCallNum") String customerCallNum,
                                             @Param("quoteChannelId") Integer quoteChannelId,
                                             @Param("quoteCounselor") String quoteCounselor
    );
    // 페이징 포함 조회
    List<QuoteQueryResponseDTO> getQuoteListWithPaging(
                                                        @Param("customerName") String customerName,
                                                        @Param("customerInCharge") String customerInCharge,
                                                        @Param("customerCallNum") String customerCallNum,
                                                        @Param("quoteChannelId") Integer quoteChannelId,
                                                        @Param("quoteCounselor") String quoteCounselor,
                                                        @Param("offset") int offset,
                                                        @Param("limit") int limit
    );

    // Detail 조회
    QuoteDetailQueryResponseDTO getQuoteDetail(
                                                @Param("quoteId") Long quoteId
    );



    // 견적/상담 전체 건수
    long countQuoteList(
                        @Param("customerName") String customerName,
                        @Param("customerInCharge") String customerInCharge,
                        @Param("customerCallNum") String customerCallNum,
                        @Param("quoteChannelId") Integer quoteChannelId,
                        @Param("quoteCounselor") String quoteCounselor
    );
}
