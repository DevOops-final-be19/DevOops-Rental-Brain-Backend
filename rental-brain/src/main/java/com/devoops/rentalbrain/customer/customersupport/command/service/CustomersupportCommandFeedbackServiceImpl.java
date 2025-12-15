package com.devoops.rentalbrain.customer.customersupport.command.service;

import com.devoops.rentalbrain.common.codegenerator.CodeGenerator;
import com.devoops.rentalbrain.common.codegenerator.CodeType;
import com.devoops.rentalbrain.customer.customersupport.command.dto.FeedbackDTO;
import com.devoops.rentalbrain.customer.customersupport.command.entity.CustomersupportCommandFeedbackEntity;
import com.devoops.rentalbrain.customer.customersupport.command.repository.CustomersupportCommandFeedbackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CustomersupportCommandFeedbackServiceImpl implements CustomersupportCommandFeedbackService {

    private final CustomersupportCommandFeedbackRepository repository;
    private final ModelMapper modelMapper;
    private final CodeGenerator codeGenerator;

    @Override   // 등록
    public Long registerFeedback(FeedbackDTO dto) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        CustomersupportCommandFeedbackEntity entity = modelMapper.map(dto, CustomersupportCommandFeedbackEntity.class);
        String code = codeGenerator.generate(CodeType.FEEDBACK);
        entity.setFeedbackCode(code);

        return repository.save(entity).getId();
    }

    @Override   // 수정
    public void updateFeedback(Long id, FeedbackDTO dto) {
        CustomersupportCommandFeedbackEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 피드백이 없습니다. ID=" + id));

        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(dto, entity);
    }

    @Override   // 삭제
    public void deleteFeedback(Long id) {
        repository.deleteById(id);
    }
}