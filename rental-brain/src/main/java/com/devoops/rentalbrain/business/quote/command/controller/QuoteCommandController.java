package com.devoops.rentalbrain.business.quote.command.controller;

import com.devoops.rentalbrain.business.quote.command.dto.QuoteCommandCreateDTO;
import com.devoops.rentalbrain.business.quote.command.dto.QuoteCommandResponseDTO;
import com.devoops.rentalbrain.business.quote.command.service.QuoteCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quote")
@Slf4j
public class QuoteCommandController {
    private final QuoteCommandService quoteCommandService;


    @Autowired
    public QuoteCommandController(QuoteCommandService quoteCommandService) {
        this.quoteCommandService = quoteCommandService;
    }

    @PostMapping("/insertQuote")
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

    @PutMapping("/updateQuote/{quoteId}")
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

    @DeleteMapping("/deleteQuote/{quoteId}")
    public ResponseEntity<QuoteCommandResponseDTO> deleteQuote(@PathVariable Long quoteId) {
        quoteCommandService.deleteQuote(quoteId);
        log.info("견적상담 삭제 완료: id={}", quoteId);
        return ResponseEntity.noContent().build();
    }

}
