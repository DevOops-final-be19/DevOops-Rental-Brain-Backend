package com.devoops.rentalbrain.business.quote.command.service;

import com.devoops.rentalbrain.business.quote.command.dto.QuoteCommandCreateDTO;
import com.devoops.rentalbrain.business.quote.command.dto.QuoteCommandResponseDTO;
import com.devoops.rentalbrain.business.quote.command.entity.QuoteCommandEntity;
import com.devoops.rentalbrain.business.quote.command.repository.QuoteCommandRepository;
import com.devoops.rentalbrain.common.codegenerator.CodeGenerator;
import com.devoops.rentalbrain.common.codegenerator.CodeType;
import com.devoops.rentalbrain.common.notice.application.domain.PositionType;
import com.devoops.rentalbrain.common.notice.application.strategy.event.QuoteInsertedEvent;
import com.devoops.rentalbrain.common.notice.application.facade.NotificationPublisher;
import com.devoops.rentalbrain.customer.customerlist.command.entity.CustomerlistCommandEntity;
import com.devoops.rentalbrain.customer.customerlist.command.repository.CustomerlistCommandRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuoteCommandServiceImpl implements QuoteCommandService {

    private final QuoteCommandRepository quoteCommandRepository;
    private final CustomerlistCommandRepository customerlistCommandRepository;
    private final CodeGenerator codeGenerator;
    private final NotificationPublisher notificationPublisher;

    @Override
    @Transactional
    public QuoteCommandCreateDTO insertQuote(QuoteCommandCreateDTO quoteCommandCreateDTO) {

        //  고객 존재 여부 최소 검증
        CustomerlistCommandEntity customer = customerlistCommandRepository
                .findById(quoteCommandCreateDTO.getQuoteCumId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "존재하지 않는 고객입니다. cum_id=" + quoteCommandCreateDTO.getQuoteCumId()));

        if ("Y".equals(customer.getIsDeleted())) {
            throw new IllegalArgumentException("삭제된 고객입니다. cum_id=" + quoteCommandCreateDTO.getQuoteCumId());
        }

        // 변경: 테이블_code 생성
        String quoteCode = codeGenerator.generate(CodeType.QUOTE); // 예: QUO-2025-001
        quoteCommandCreateDTO.setQuoteCode(quoteCode);

        // quote 엔티티 저장할 정보 및 Body에 담을 것
        QuoteCommandEntity entity = new QuoteCommandEntity();
        entity.setQuoteCode(quoteCode);  // 엔티티에 업무 코드 세팅
        entity.setQuoteCounselingDate(quoteCommandCreateDTO.getQuoteCounselingDate());
        entity.setQuoteCounselor(quoteCommandCreateDTO.getQuoteCounselor());
        entity.setQuoteSummary(quoteCommandCreateDTO.getQuoteSummary());
        entity.setQuoteContent(quoteCommandCreateDTO.getQuoteContent());
        entity.setQuoteProcessingTime(quoteCommandCreateDTO.getQuoteProcessingTime());
        entity.setQuoteChannelId(quoteCommandCreateDTO.getQuoteChannelId());
        entity.setQuoteCumId(quoteCommandCreateDTO.getQuoteCumId());

        QuoteCommandEntity saved = quoteCommandRepository.save(entity);

        // 필요하면 dto에 id 세팅해서 반환
        quoteCommandCreateDTO.setQuoteId(saved.getQuoteId());

        // 해당 부서에 알림 전송
        notificationPublisher.publish(new QuoteInsertedEvent(List.of(PositionType.BUSINESS,PositionType.BUSINESS_MANAGER),customer.getName(),customer.getId()));

        return quoteCommandCreateDTO;
    }

    @Override
    @Transactional
    public QuoteCommandResponseDTO updateQuote(Long quoteId, QuoteCommandResponseDTO quoteCommandResponseDTO) {
        QuoteCommandEntity quote = quoteCommandRepository.findById(quoteId.longValue())
                .orElseThrow(() -> new EntityNotFoundException("수정할 견적을 찾을 수 없습니다. id=" + quoteId));

        // 수정할 필드 매핑 (null 이 아닐 때 즉 정보를 입력하면 그때 적용하겠다는 의미)
        if (quoteCommandResponseDTO.getQuoteCounselingDate() != null) {
            quote.setQuoteCounselingDate(quoteCommandResponseDTO.getQuoteCounselingDate());
        }
        if (quoteCommandResponseDTO.getQuoteCounselor() != null) {
            quote.setQuoteCounselor(quoteCommandResponseDTO.getQuoteCounselor());
        }
        if (quoteCommandResponseDTO.getQuoteSummary() != null) {
            quote.setQuoteSummary(quoteCommandResponseDTO.getQuoteSummary());
        }
        if (quoteCommandResponseDTO.getQuoteContent() != null) {
            quote.setQuoteContent(quoteCommandResponseDTO.getQuoteContent());
        }
        if (quoteCommandResponseDTO.getQuoteProcessingTime() != null) {
            quote.setQuoteProcessingTime(quoteCommandResponseDTO.getQuoteProcessingTime());
        }
        if (quoteCommandResponseDTO.getQuoteChannelId() != null) {
            quote.setQuoteChannelId(quoteCommandResponseDTO.getQuoteChannelId());
        }
        if (quoteCommandResponseDTO.getQuoteCumId() != null) {
            quote.setQuoteCumId(quoteCommandResponseDTO.getQuoteCumId());
        }

        // 수정할 정보
        QuoteCommandEntity saved = quoteCommandRepository.save(quote);


        // 응답 부분
        QuoteCommandResponseDTO reqSaved = new QuoteCommandResponseDTO();
        reqSaved.setQuoteId(saved.getQuoteId());
        reqSaved.setQuoteCounselingDate(saved.getQuoteCounselingDate());
        reqSaved.setQuoteCounselor(saved.getQuoteCounselor());
        reqSaved.setQuoteSummary(saved.getQuoteSummary());
        reqSaved.setQuoteContent(saved.getQuoteContent());
        reqSaved.setQuoteProcessingTime(saved.getQuoteProcessingTime());
        reqSaved.setQuoteChannelId(saved.getQuoteChannelId());
        reqSaved.setQuoteCumId(saved.getQuoteCumId());

        return reqSaved;
    }

    @Override
    @Transactional
    public void deleteQuote(Long quoteId) {
        if(!quoteCommandRepository.existsById(quoteId)) {
            throw new EntityNotFoundException("삭제할 기록이 없습니다. id=" + quoteId);
        }
        quoteCommandRepository.deleteById(quoteId);
    }
}
