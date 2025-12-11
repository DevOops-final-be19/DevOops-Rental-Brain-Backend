package com.devoops.rentalbrain.business.quote.query.controller;

import com.devoops.rentalbrain.business.quote.query.dto.QuoteDetailQueryResponseDTO;
import com.devoops.rentalbrain.business.quote.query.dto.QuoteKpiResponseDTO;
import com.devoops.rentalbrain.business.quote.query.dto.QuoteQueryResponseDTO;
import com.devoops.rentalbrain.business.quote.query.service.QuoteQueryService;
import com.devoops.rentalbrain.common.Pagination.Criteria;
import com.devoops.rentalbrain.common.Pagination.PageResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/quote")
public class QuoteQueryController {

    private final QuoteQueryService quoteQueryService;

    @Autowired
    public QuoteQueryController(QuoteQueryService quoteQueryService) {
        this.quoteQueryService = quoteQueryService;
    }

    @GetMapping("/all")
    public ResponseEntity<PageResponseDTO<QuoteQueryResponseDTO>> getQuoteList(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String customerInCharge,
            @RequestParam(required = false) String customerCallNum,
            @RequestParam(required = false) Integer quoteChannelId,
            @RequestParam(required = false) String quoteCounselor,
            @RequestParam(defaultValue = "1") int page,         // 페이징
            @RequestParam(defaultValue = "10") int size         // 페이징
    ) {

        // 공용 Criteria 사용 (페이지 정보만 사용)
        Criteria criteria = new Criteria(page, size);

        PageResponseDTO<QuoteQueryResponseDTO> pageResponse =
                quoteQueryService.getQuoteListWithPaging(
                        customerName,
                        customerInCharge,
                        customerCallNum,
                        quoteChannelId,
                        quoteCounselor,
                        criteria
                );

        log.info("견적/상담 페이지 조회 - page: {}, size: {}, totalCount: {}",
                page, size, pageResponse.getTotalCount());

        return ResponseEntity.ok(pageResponse);
    }

    // 견적 상세 조회
    @GetMapping("/{quoteId}")
    public ResponseEntity<QuoteDetailQueryResponseDTO> getQuote(
                                                                @PathVariable("quoteId") Long quoteId
    ) {
        // 조회 결과가 없으면 → HTTP 404 (Not Found) 반환
        QuoteDetailQueryResponseDTO detail
                = quoteQueryService.getQuoteDetail(quoteId);

        if (detail == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(detail);
    }

    // kpi 카드 조회
    @GetMapping("/kpi")
    public ResponseEntity<QuoteKpiResponseDTO> getQuoteKpi() {

        QuoteKpiResponseDTO kpi = quoteQueryService.getQuoteKpi();

        log.info("견적/상담 KPI 조회: {}", kpi);

        return ResponseEntity.ok(kpi);
    }
}
