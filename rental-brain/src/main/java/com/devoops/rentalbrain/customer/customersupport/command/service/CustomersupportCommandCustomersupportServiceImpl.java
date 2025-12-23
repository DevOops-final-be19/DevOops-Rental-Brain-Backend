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

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CustomersupportCommandCustomersupportServiceImpl implements CustomersupportCommandCustomersupportService {

    private final CustomersupportCommandCustomersupportRepository repository;
    private final CodeGenerator codeGenerator; // [중요] 코드 생성기 주입

    @Override
    public Long registerSupport(CustomersupportDTO dto) {
        CustomersupportCommandCustomersupportEntity entity = new CustomersupportCommandCustomersupportEntity();

        // 1. 코드 자동 생성 (CSU-2025-XXX)
        String newCode = codeGenerator.generate(CodeType.CUSTOMER_SUPPORT);
        entity.setCustomerSupportCode(newCode);

        // 2. 기본 정보 매핑
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setStatus("P"); // 기본값 '진행 중'
        entity.setCreateDate(LocalDateTime.now());

        // 3. ID 매핑 (이 부분이 누락되어 에러가 났던 것)
        entity.setCumId(dto.getCustomerId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setChannelId(dto.getChannelId());

        // 담당자 ID (선택 안 했으면 1번 관리자로 설정 예시)
        if (dto.getEmpId() != null) {
            entity.setEmpId(dto.getEmpId());
        } else {
            entity.setEmpId(1L);
        }

        repository.save(entity);
        log.info("신규 문의 등록 성공: ID={}, Code={}", entity.getId(), newCode);

        return entity.getId();
    }

    @Override
    public void updateSupport(Long id, CustomersupportDTO dto) {
        CustomersupportCommandCustomersupportEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 문의가 없습니다. id=" + id));

        // 수정 가능한 필드만 업데이트 (Dirty Checking으로 자동 저장)
        if(dto.getTitle() != null) entity.setTitle(dto.getTitle());
        if(dto.getContent() != null) entity.setContent(dto.getContent());
        if(dto.getCategoryId() != null) entity.setCategoryId(dto.getCategoryId());
        if(dto.getChannelId() != null) entity.setChannelId(dto.getChannelId());
        if(dto.getEmpId() != null) entity.setEmpId(dto.getEmpId());
        // 조치사항(Action) 수정 기능
        if(dto.getAction() != null) entity.setAction(dto.getAction());
        // 상태(Status) 변경 기능 (진행중 -> 완료)
        if(dto.getStatus() != null) entity.setStatus(dto.getStatus());
    }

    @Override   // 삭제
    public void deleteSupport(Long id) {
        repository.deleteById(id);
    }
}