package com.devoops.rentalbrain.customer.customersupport.command.service;

import com.devoops.rentalbrain.common.codegenerator.CodeGenerator;
import com.devoops.rentalbrain.common.codegenerator.CodeType;
import com.devoops.rentalbrain.common.error.ErrorCode; // [중요] ErrorCode import
import com.devoops.rentalbrain.common.error.exception.EntityNotFoundException;
import com.devoops.rentalbrain.customer.customersupport.command.entity.CustomersupportCommandFeedbackEntity;
import com.devoops.rentalbrain.customer.customersupport.command.repository.CustomersupportCommandFeedbackRepository;
import com.devoops.rentalbrain.customer.customersupport.query.dto.FeedbackDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomersupportCommandFeedbackServiceImpl implements CustomersupportCommandFeedbackService {

    private final CustomersupportCommandFeedbackRepository feedbackRepository;
    private final CodeGenerator codeGenerator;

    @Override
    public void createFeedback(FeedbackDTO dto) {
        String feedbackCode = codeGenerator.generate(CodeType.FEEDBACK);

        CustomersupportCommandFeedbackEntity entity = CustomersupportCommandFeedbackEntity.builder()
                .feedbackCode(feedbackCode)
                .title(dto.getTitle())
                .content(dto.getContent())
                .star(dto.getStar())
                .action(dto.getAction())
                .cumId(dto.getCumId())
                .empId(dto.getEmpId())
                .categoryId(dto.getCategoryId())
                .channelId(dto.getChannelId())
                .build();

        feedbackRepository.save(entity);
    }

    @Override
    public void updateFeedback(Long id, FeedbackDTO dto) {
        CustomersupportCommandFeedbackEntity entity = feedbackRepository.findById(id)
                // [수정] 문자열 대신 ErrorCode.FEEDBACK_NOT_FOUND 사용
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.FEEDBACK_NOT_FOUND));

        entity.updateFeedback(
                dto.getTitle(),
                dto.getContent(),
                dto.getStar(),
                dto.getAction(),
                dto.getEmpId(),
                dto.getCategoryId(),
                dto.getChannelId()
        );
    }

    @Override
    public void deleteFeedback(Long id) {
        if (!feedbackRepository.existsById(id)) {
            // [수정] 문자열 대신 ErrorCode.FEEDBACK_NOT_FOUND 사용
            throw new EntityNotFoundException(ErrorCode.FEEDBACK_NOT_FOUND);
        }
        feedbackRepository.deleteById(id);
    }
}