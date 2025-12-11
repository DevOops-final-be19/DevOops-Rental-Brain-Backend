package com.devoops.rentalbrain.customer.customerlist.command.service;

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

    private final CustomerlistCommandRepository CustomerlistCommandRepository;
    private final ModelMapper modelMapper;

    // 등록
    @Override
    public Long registerCustomer(CustomerlistCommandDTO dto) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        CustomerlistCommandEntity customer = modelMapper.map(dto, CustomerlistCommandEntity.class);

        // (isDeleted는 null로 들어가면 @DynamicInsert에 의해 DB Default 'N' 적용됨)

        return CustomerlistCommandRepository.save(customer).getId();
    }

    // 수정 (ModelMapper 사용)
    @Override
    public void updateCustomer(Long id, CustomerlistCommandDTO dto) {
        CustomerlistCommandEntity customer = CustomerlistCommandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 고객이 없습니다. ID=" + id));

        // null 값은 건너뛰고(Skip) 값이 있는 필드만 덮어쓰기
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT)
                                      .setSkipNullEnabled(true);
        modelMapper.map(dto, customer);

        // Transaction 종료 시 Dirty Checking으로 자동 Update 쿼리 실행
    }

    // 삭제 (Soft Delete)
    @Override
    public void deleteCustomer(Long id) {
        CustomerlistCommandEntity customer = CustomerlistCommandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 고객이 없습니다. ID=" + id));

        customer.setIsDeleted("Y");
        log.info("고객 삭제(Soft Delete) 완료: ID={}", id);
    }

    // [복구] Soft Delete 해제 (Y -> N)
    @Override
    public void restoreCustomer(Long id) {
        CustomerlistCommandEntity customer = CustomerlistCommandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 고객이 없습니다. ID=" + id));

        customer.setIsDeleted("N"); // 상태를 다시 'N'으로 변경
        log.info("고객 복구(Restore) 완료: ID={}", id);
    }
}