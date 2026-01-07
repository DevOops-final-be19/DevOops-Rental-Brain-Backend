package com.devoops.rentalbrain.product.maintenance.command.service;

import com.devoops.rentalbrain.common.codegenerator.CodeGenerator;
import com.devoops.rentalbrain.common.codegenerator.CodeType;
import com.devoops.rentalbrain.common.error.exception.BusinessException;
import com.devoops.rentalbrain.common.error.exception.EntityNotFoundException;
import com.devoops.rentalbrain.common.error.ErrorCode;
import com.devoops.rentalbrain.product.maintenance.command.dto.AfterServiceCreateRequest;
import com.devoops.rentalbrain.product.maintenance.command.dto.AfterServiceUpdateRequest;
import com.devoops.rentalbrain.product.maintenance.command.entity.AfterService;
import com.devoops.rentalbrain.product.maintenance.command.repository.AfterServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AfterServiceCommandServiceImpl implements AfterServiceCommandService {

    private final AfterServiceRepository repository;
    private final CodeGenerator codeGenerator;

    /**
     * AS / 정기점검 생성
     */
    @Override
    public void create(AfterServiceCreateRequest request) {

        // ️필수 값 검증
        if (request.getItemId() == null ||
                request.getDueDate() == null ||
                request.getType() == null) {

            throw new BusinessException(
                    ErrorCode.AS_INVALID_REQUEST,
                    "AS/정기점검 생성 필수 값 누락"
            );
        }

        // type 값 검증
        if (!"A".equals(request.getType()) && !"R".equals(request.getType())) {
            throw new BusinessException(
                    ErrorCode.AS_INVALID_REQUEST,
                    "type은 A(AS) 또는 R(정기점검)만 가능합니다."
            );
        }

        // 동일 자산 + 동일 예정일 중복 방지
        if (repository.existsByItemIdAndDueDate(
                request.getItemId(), request.getDueDate())) {

            throw new BusinessException(
                    ErrorCode.AS_DUPLICATE_SCHEDULE,
                    "이미 동일 자산/동일 예정일의 점검 일정이 존재합니다."
            );
        }

        // 업무 코드 생성
        String afterServiceCode = codeGenerator.generate(CodeType.AFTER_SERVICE);

        // 엔티티 생성
        AfterService entity = AfterService.builder()
                .after_service_code(afterServiceCode)
                .engineer(request.getEngineer())
                .type(request.getType())          // A / R
                .dueDate(request.getDueDate())
                .status("P")                      // 예정
                .contents(request.getContents())
                .itemId(request.getItemId())
                .customerId(request.getCustomerId())
                .build();

        repository.save(entity);

        log.info("AS/정기점검 생성 완료. id={}, code={}",
                entity.getId(), afterServiceCode);
    }

    /**
     * AS / 정기점검 수정
     */
    @Override
    public void update(Long asId, AfterServiceUpdateRequest request) {

        AfterService entity = repository.findById(asId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                ErrorCode.AS_NOT_FOUND,
                                "수정할 AS/정기점검이 존재하지 않습니다. id=" + asId
                        ));

        // status 수정 요청이 들어온 경우만 검증
        if (request.getStatus() != null) {
            if (!"C".equals(request.getStatus()) && !"P".equals(request.getStatus())) {
                throw new BusinessException(
                        ErrorCode.AS_INVALID_REQUEST,
                        "status은 P(예정) 또는 C(완료)만 가능합니다."
                );
            }
            entity.setStatus(request.getStatus());
        }

        // 부분 수정
        if (request.getEngineer() != null) {
            entity.setEngineer(request.getEngineer());
        }
        if (request.getDueDate() != null) {
            entity.setDueDate(request.getDueDate());
        }
        if (request.getContents() != null) {
            entity.setContents(request.getContents());
        }

        log.info("AS/정기점검 수정 완료. id={}", asId);
    }

    @Override
    public void autoCompleteAndCreateNext() {

        // 예정 → 완료 처리 대상 조회
        List<AfterService> expiredList =
                repository.findByStatusAndDueDateBefore("P", LocalDateTime.now());

        if (expiredList.isEmpty()) {
            return;
        }

        for (AfterService as : expiredList) {

            // 완료 처리
            as.setStatus("C");

            // 정기점검(R)만 다음 일정 생성
            if (!"R".equals(as.getType())) {
                continue;
            }

            LocalDateTime nextDueDate = as.getDueDate().plusMonths(1);

            // 중복 방지
            if (repository.existsByItemIdAndDueDate(as.getItemId(), nextDueDate)) {
                continue;
            }

            String newCode = codeGenerator.generate(CodeType.AFTER_SERVICE);

            AfterService next = AfterService.builder()
                    .after_service_code(newCode)
                    .type("R")
                    .engineer(as.getEngineer())
                    .dueDate(nextDueDate)
                    .status("P")
                    .contents("정기점검 자동 생성")
                    .itemId(as.getItemId())
                    .customerId(as.getCustomerId())
                    .build();

            repository.save(next);

            log.info(
                    "정기점검 완료 → 다음 일정 생성. prevId={}, newCode={}",
                    as.getId(), newCode
            );
        }
    }

    /**
     * AS / 정기점검 삭제
     */
    @Override
    public void delete(Long asId) {

        AfterService entity = repository.findById(asId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                ErrorCode.AS_NOT_FOUND,
                                "삭제할 AS/정기점검이 존재하지 않습니다. id=" + asId
                        ));

        repository.delete(entity);

        log.info("AS/정기점검 삭제 완료. id={}, code={}",
                entity.getId(), entity.getAfter_service_code());
    }


}
