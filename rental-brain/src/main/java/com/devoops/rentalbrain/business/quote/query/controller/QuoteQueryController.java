package com.devoops.rentalbrain.business.quote.query.controller;

import com.devoops.rentalbrain.business.quote.query.dto.QuoteDetailQueryResponseDTO;
import com.devoops.rentalbrain.business.quote.query.dto.QuoteKpiResponseDTO;
import com.devoops.rentalbrain.business.quote.query.dto.QuoteQueryResponseDTO;
import com.devoops.rentalbrain.business.quote.query.service.QuoteQueryService;
import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/quote")
@Tag(
        name = "견적/상담 조회(Query)",
        description = "견적(상담) 목록 조회, 상세 조회, KPI 조회 API"
)
public class QuoteQueryController {

    private final QuoteQueryService quoteQueryService;

    @Autowired
    public QuoteQueryController(QuoteQueryService quoteQueryService) {
        this.quoteQueryService = quoteQueryService;
    }

    // 견적/상담 목록 조회 (검색 + 페이징)
    @Operation(
            summary = "견적/상담 목록 조회",
            description = "검색 조건과 페이징 정보를 이용하여 견적(상담) 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
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
    @Operation(
            summary = "견적/상담 상세 조회",
            description = "quoteId에 해당하는 견적(상담) 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 견적 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
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

    // KPI 카드 조회
    @Operation(
            summary = "견적/상담 KPI 조회",
            description = "견적 및 상담 현황을 요약한 KPI 데이터를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    // kpi 카드 조회
    @GetMapping("/kpi")
    public ResponseEntity<QuoteKpiResponseDTO> getQuoteKpi() {

        QuoteKpiResponseDTO kpi = quoteQueryService.getQuoteKpi();

        log.info("견적/상담 KPI 조회: {}", kpi);

        return ResponseEntity.ok(kpi);
    }
}
