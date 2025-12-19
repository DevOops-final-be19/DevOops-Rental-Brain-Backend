package com.devoops.rentalbrain.customer.customerlist.command.service;

import com.devoops.rentalbrain.common.codegenerator.CodeGenerator;
import com.devoops.rentalbrain.common.codegenerator.CodeType;
import com.devoops.rentalbrain.customer.customerlist.command.dto.CustomerlistCommandDTO;
import com.devoops.rentalbrain.customer.customerlist.command.entity.CustomerlistCommandEntity;
import com.devoops.rentalbrain.customer.customerlist.command.repository.CustomerlistCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies; // ★ 이 import가 꼭 필요합니다!
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomerlistCommandServiceImpl implements CustomerlistCommandService {

    private final CustomerlistCommandRepository customerRepository;
    private final CodeGenerator codeGenerator;

    // ★ 수정 포인트 1: ModelMapper를 주입받지 않고, 직접 생성하여 이 클래스 전용으로 만듭니다.
    // (필드 초기화를 했기 때문에 @RequiredArgsConstructor 생성자 주입 대상에서 제외됨)
    private final ModelMapper modelMapper = new ModelMapper();

    // ★ 수정 포인트 2: 인스턴스 초기화 블록을 통해 매핑 전략을 'STRICT'로 설정합니다.
    {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    public Long registerCustomer(CustomerlistCommandDTO dto) {
        // STRICT 전략이 적용된 로컬 modelMapper를 사용하므로 에러가 발생하지 않습니다.
        CustomerlistCommandEntity customer = modelMapper.map(dto, CustomerlistCommandEntity.class);

        // 1. 코드 생성
        customer.setCustomerCode(codeGenerator.generate(CodeType.CUSTOMER));

        // 2. 외래키 ID 기본값 설정
        if (customer.getSegmentId() == null) {
            customer.setSegmentId(1L); // 기본값: 잠재 고객
        }
        if (customer.getChannelId() == null) {
            customer.setChannelId(6L); // 기본값: 기타
        }

        customer.setIsDeleted("N");
        CustomerlistCommandEntity saved = customerRepository.save(customer);
        return saved.getId();
    }

    @Override
    public void updateCustomer(Long id, CustomerlistCommandDTO dto) {
        CustomerlistCommandEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 고객입니다."));

        // modelMapper를 사용하여 DTO -> Entity 덮어쓰기 (null 값은 건너뛰도록 설정도 가능하지만, 여기선 수동 매핑 유지 추천)
        // 기존 수동 매핑 유지 (안전함)
        if (dto.getName() != null) customer.setName(dto.getName());
        if (dto.getInCharge() != null) customer.setInCharge(dto.getInCharge());
        if (dto.getDept() != null) customer.setDept(dto.getDept());
        if (dto.getPhone() != null) customer.setPhone(dto.getPhone());
        if (dto.getEmail() != null) customer.setEmail(dto.getEmail());
        if (dto.getAddr() != null) customer.setAddr(dto.getAddr());
        if (dto.getMemo() != null) customer.setMemo(dto.getMemo());

        // 필요시 ID 업데이트 로직
        // if (dto.getSegmentId() != null) customer.setSegmentId(dto.getSegmentId());
    }

    @Override
    public void deleteCustomer(Long id) {
        CustomerlistCommandEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 고객입니다."));
        customer.setIsDeleted("Y");
        log.info("고객 삭제(Soft Delete) 완료: ID={}", id);

    }

    @Override
    public void restoreCustomer(Long id) {
        CustomerlistCommandEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 고객입니다."));
        customer.setIsDeleted("N");
        log.info("고객 복구(Restore) 완료: ID={}", id);
    }
}