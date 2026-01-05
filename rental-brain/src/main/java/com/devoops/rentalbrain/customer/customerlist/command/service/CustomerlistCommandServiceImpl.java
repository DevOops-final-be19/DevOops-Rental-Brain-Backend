package com.devoops.rentalbrain.customer.customerlist.command.service;

import com.devoops.rentalbrain.common.codegenerator.CodeGenerator;
import com.devoops.rentalbrain.common.codegenerator.CodeType;
import com.devoops.rentalbrain.common.notice.application.domain.PositionType;
import com.devoops.rentalbrain.common.notice.application.facade.NotificationPublisher;
import com.devoops.rentalbrain.common.notice.application.strategy.event.CustomerRegistEvent;
import com.devoops.rentalbrain.customer.customerlist.command.dto.CustomerlistCommandDTO;
import com.devoops.rentalbrain.customer.customerlist.command.entity.CustomerlistCommandEntity;
import com.devoops.rentalbrain.customer.customerlist.command.repository.CustomerlistCommandRepository;

// [추가된 Import]
import com.devoops.rentalbrain.customer.customersegmenthistory.command.dto.HistoryCommandCreateDTO;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.service.HistoryCommandService;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.domain.SegmentChangeTriggerType;
// 주의: TriggerType에 'MANUAL' 상수가 있는지 확인 필요 (없으면 해당 Enum에 추가 필요)

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomerlistCommandServiceImpl implements CustomerlistCommandService {

    private final CustomerlistCommandRepository customerRepository;
    private final CodeGenerator codeGenerator;
    private final NotificationPublisher notificationPublisher;

    // [추가] 이력 저장 서비스 주입
    private final HistoryCommandService historyCommandService;

    // ModelMapper 설정 유지
    private final ModelMapper modelMapper = new ModelMapper();

    {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    public Long registerCustomer(CustomerlistCommandDTO dto) {
        CustomerlistCommandEntity customer = modelMapper.map(dto, CustomerlistCommandEntity.class);

        customer.setCustomerCode(codeGenerator.generate(CodeType.CUSTOMER));

        if (customer.getSegmentId() == null) {
            customer.setSegmentId(1L); // 기본값: 잠재 고객
        }
        if (customer.getChannelId() == null) {
            customer.setChannelId(6L); // 기본값: 기타
        }

        customer.setIsDeleted("N");
        CustomerlistCommandEntity saved = customerRepository.save(customer);
        notificationPublisher.publish(new CustomerRegistEvent(List.of(PositionType.CUSTOMER,PositionType.CUSTOMER_MANAGER),saved.getName(),saved.getId()));
        return saved.getId();
    }

    @Override
    public void updateCustomer(Long id, CustomerlistCommandDTO dto) {
        CustomerlistCommandEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 고객입니다."));

        // [변경 전 세그먼트 ID 저장]
        Long oldSegmentId = customer.getSegmentId();

        // 기존 정보 업데이트 로직
        if (dto.getName() != null) customer.setName(dto.getName());
        if (dto.getInCharge() != null) customer.setInCharge(dto.getInCharge());
        if (dto.getDept() != null) customer.setDept(dto.getDept());
        if (dto.getPhone() != null) customer.setPhone(dto.getPhone());
        if (dto.getEmail() != null) customer.setEmail(dto.getEmail());
        if (dto.getAddr() != null) customer.setAddr(dto.getAddr());
        if (dto.getMemo() != null) customer.setMemo(dto.getMemo());

        // 세그먼트 ID 업데이트
        if (dto.getSegmentId() != null) {
            customer.setSegmentId(dto.getSegmentId());
        }

        // DB 반영 (Dirty Checking)
        customerRepository.save(customer);

        // [세그먼트 변경 감지 및 이력 저장]
        // DTO에 값이 있고, 기존 값과 다를 경우에만 실행
        if (dto.getSegmentId() != null && !dto.getSegmentId().equals(oldSegmentId)) {
            saveSegmentChangeHistory(id, oldSegmentId, dto.getSegmentId());
        }
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

    /**
     * [추가] 세그먼트 변경 이력 저장 헬퍼 메서드
     */
    private void saveSegmentChangeHistory(Long customerId, Long oldSegmentId, Long newSegmentId) {
        try {
            HistoryCommandCreateDTO historyDTO = new HistoryCommandCreateDTO();

            // 1. DTO 필드명(customerId)에 맞춰 Long 타입 그대로 설정
            historyDTO.setCustomerId(customerId);

            // 2. DTO 필드명(previousSegmentId, currentSegmentId)에 맞춰 설정
            historyDTO.setPreviousSegmentId(oldSegmentId);
            historyDTO.setCurrentSegmentId(newSegmentId);

            // 3. 사유 설정
            historyDTO.setReason("수동 변경");

            // 4. TriggerType 설정 (Enum 사용)
            // 만약 'MANUAL'이 없다면 SegmentChangeTriggerType 파일을 확인하여 적절한 값(예: ETC, ADMIN_ACTION 등)으로 변경하세요.
            historyDTO.setTriggerType(SegmentChangeTriggerType.MANUAL);

            // 5. ReferenceType은 null 또는 필요시 설정
            historyDTO.setReferenceType(null);
            historyDTO.setReferenceId(null);

            // 6. 서비스 호출
            // HistoryCommandService에 'createHistory' 메서드가 없다면 'create', 'save', 'register' 중 하나일 것입니다.
            // 아래 줄에서 에러가 나면 historyCommandService 파일을 열어 메서드 이름을 확인하세요.
            historyCommandService.create(historyDTO);

            log.info("세그먼트 변경 이력 저장 완료: CustomerID={}, {} -> {}", customerId, oldSegmentId, newSegmentId);

        } catch (Exception e) {
            log.error("세그먼트 변경 이력 저장 실패: {}", e.getMessage());
            // 이력 저장 실패가 고객 수정 트랜잭션을 롤백시키지 않도록 하려면 try-catch 유지
        }
    }
}