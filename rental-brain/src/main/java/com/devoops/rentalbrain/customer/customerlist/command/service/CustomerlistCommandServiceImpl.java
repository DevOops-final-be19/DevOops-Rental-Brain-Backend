package com.devoops.rentalbrain.customer.customerlist.command.service;

import com.devoops.rentalbrain.common.codegenerator.CodeGenerator;
import com.devoops.rentalbrain.common.codegenerator.CodeType;
import com.devoops.rentalbrain.customer.customerlist.command.dto.CustomerlistCommandDTO;
import com.devoops.rentalbrain.customer.customerlist.command.entity.CustomerlistCommandEntity;
import com.devoops.rentalbrain.customer.customerlist.command.repository.CustomerlistCommandRepository;
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
public class CustomerlistCommandServiceImpl implements CustomerlistCommandService {

    private final CustomerlistCommandRepository customerlistCommandRepository;
    private final ModelMapper modelMapper;
    private final CodeGenerator codeGenerator; // [추가] 채번기 주입

    // 등록
    @Override
    public Long registerCustomer(CustomerlistCommandDTO dto) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        CustomerlistCommandEntity customer = modelMapper.map(dto, CustomerlistCommandEntity.class);

        // [추가] 고객 코드 생성 (CUS-YYYY-NNN)
        String customerCode = codeGenerator.generate(CodeType.CUSTOMER);
        customer.setCustomerCode(customerCode);

        // (isDeleted는 @DynamicInsert로 처리)

        return customerlistCommandRepository.save(customer).getId();
    }

    // 수정
    @Override
    public void updateCustomer(Long id, CustomerlistCommandDTO dto) {
        CustomerlistCommandEntity customer = customerlistCommandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 고객이 없습니다. ID=" + id));

        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(dto, customer);

        // 코드(customerCode)는 수정하지 않음 (한 번 생성되면 고정)
    }

    // 삭제 (Soft Delete)
    @Override
    public void deleteCustomer(Long id) {
        CustomerlistCommandEntity customer = customerlistCommandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 고객이 없습니다. ID=" + id));

        customer.setIsDeleted("Y");
        log.info("고객 삭제(Soft Delete) 완료: ID={}", id);
    }

    // 복구
    @Override
    public void restoreCustomer(Long id) {
        CustomerlistCommandEntity customer = customerlistCommandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 고객이 없습니다. ID=" + id));

        customer.setIsDeleted("N");
        log.info("고객 복구(Restore) 완료: ID={}", id);
    }
}