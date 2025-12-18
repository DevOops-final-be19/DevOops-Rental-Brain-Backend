package com.devoops.rentalbrain.business.quote.command.controller;

import com.devoops.rentalbrain.business.quote.command.dto.QuoteCommandCreateDTO;
import com.devoops.rentalbrain.business.quote.command.dto.QuoteCommandResponseDTO;
import com.devoops.rentalbrain.business.quote.command.service.QuoteCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quote")
@Slf4j
@Tag(
        name = "견적/상담 관리(Command)",
        description = "견적(상담) 등록, 수정, 삭제 등 명령(Command) API"
)
public class QuoteCommandController {
    private final QuoteCommandService quoteCommandService;

    @Autowired
    public QuoteCommandController(QuoteCommandService quoteCommandService) {
        this.quoteCommandService = quoteCommandService;
    }

    @Operation(
            summary = "견적/상담 등록",
            description = "신규 견적(상담) 내용을 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "400", description = "요청 값 검증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/insert")
    public ResponseEntity<QuoteCommandCreateDTO> insertQuote(
            @RequestBody QuoteCommandCreateDTO quoteCommandCreateDTO) {

        // insert 할 DTO들 저장하는 것
        QuoteCommandCreateDTO saved
                = quoteCommandService.insertQuote(quoteCommandCreateDTO);

        if (saved == null) {
            log.info("견적/상담 저장 실패: {}", quoteCommandCreateDTO);
            throw new IllegalArgumentException("견적/상담 저장 실패했습니다.");
        } else {
            log.info("견적/상담 저장 완료!: {}", saved);
            return ResponseEntity.ok().body(saved);
        }
    }

    @Operation(
            summary = "견적/상담 수정",
            description = "quoteId에 해당하는 견적(상담) 정보를 수정합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "요청 값 검증 실패"),
            @ApiResponse(responseCode = "404", description = "대상 데이터 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/update/{quoteId}")
    public ResponseEntity<QuoteCommandResponseDTO> updateQuote(
    // 회원 확인 할라면 PathVariable 사용 @PutMapping("/updateOoh/{quoteId}")
    @PathVariable Long quoteId,
    @RequestBody QuoteCommandResponseDTO quoteCommandResponseDTO) {

        // DTO에 셋팅
        quoteCommandResponseDTO.setQuoteId(quoteId);
        // 서비스 호출
        QuoteCommandResponseDTO updated = quoteCommandService.updateQuote(
                quoteId, quoteCommandResponseDTO);

        // 수정 실패 시
        if (updated == null) {
            log.warn("견적/상담 수정 실패: quoteId={}", quoteId);
            throw new IllegalArgumentException("견적/상담 수정에 실패했습니다.");
        }

        log.info("견적/상담 수정 완료: quoteId={}, summary={}",
                updated.getQuoteId(), updated.getQuoteSummary());

        return ResponseEntity.ok(updated);

    }

    @Operation(
            summary = "견적/상담 삭제",
            description = "quoteId에 해당하는 견적(상담) 정보를 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "대상 데이터 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/delete/{quoteId}")
    public ResponseEntity<QuoteCommandResponseDTO> deleteQuote(@PathVariable Long quoteId) {
        quoteCommandService.deleteQuote(quoteId);
        log.info("견적상담 삭제 완료: id={}", quoteId);
        return ResponseEntity.noContent().build();
    }

}
