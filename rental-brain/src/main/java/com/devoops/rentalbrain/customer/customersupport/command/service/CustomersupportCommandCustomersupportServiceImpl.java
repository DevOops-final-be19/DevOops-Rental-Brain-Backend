package com.devoops.rentalbrain.customer.customersupport.command.service;

import com.devoops.rentalbrain.common.codegenerator.CodeGenerator;
import com.devoops.rentalbrain.common.codegenerator.CodeType;
import com.devoops.rentalbrain.customer.customersupport.command.dto.CustomersupportDTO;
import com.devoops.rentalbrain.customer.customersupport.command.entity.CustomersupportCommandCustomersupportEntity;
import com.devoops.rentalbrain.customer.customersupport.command.repository.CustomersupportCommandCustomersupportRepository;
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
public class CustomersupportCommandCustomersupportServiceImpl implements CustomersupportCommandCustomersupportService {

    private final CustomersupportCommandCustomersupportRepository repository;
    private final ModelMapper modelMapper;
    private final CodeGenerator codeGenerator;

    @Override   // 추가
    public Long registerSupport(CustomersupportDTO dto) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        CustomersupportCommandCustomersupportEntity entity = modelMapper.map(dto, CustomersupportCommandCustomersupportEntity.class);
        String code = codeGenerator.generate(CodeType.CUSTOMER_SUPPORT);
        entity.setCustomerSupportCode(code);

        return repository.save(entity).getId();
    }

    @Override   // 수정
    public void updateSupport(Long id, CustomersupportDTO dto) {
        CustomersupportCommandCustomersupportEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 문의가 없습니다. ID=" + id));

        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(dto, entity);
    }

    @Override   // 삭제
    public void deleteSupport(Long id) {
        repository.deleteById(id);
    }
}