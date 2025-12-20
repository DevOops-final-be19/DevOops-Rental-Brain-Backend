package com.devoops.rentalbrain.approval.command.service;


import com.devoops.rentalbrain.approval.command.Repository.ApprovalCommandRepository;
import com.devoops.rentalbrain.approval.command.Repository.ApprovalMappingCommandRepository;
import com.devoops.rentalbrain.approval.command.entity.ApprovalCommandEntity;
import com.devoops.rentalbrain.approval.command.entity.ApprovalMappingCommandEntity;
import com.devoops.rentalbrain.business.contract.command.entity.ContractCommandEntity;
import com.devoops.rentalbrain.business.contract.command.entity.PaymentDetailCommandEntity;
import com.devoops.rentalbrain.business.contract.command.repository.ContractCommandRepository;
import com.devoops.rentalbrain.business.contract.command.repository.PaymentDetailCommandRepository;
import com.devoops.rentalbrain.common.error.ErrorCode;
import com.devoops.rentalbrain.common.error.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ApprovalCommandServiceImpl implements ApprovalCommandService {

    private final ApprovalMappingCommandRepository approvalMappingCommandRepository;
    private final ApprovalCommandRepository approvalCommandRepository;
    private final ContractCommandRepository contractCommandRepository;
    private final PaymentDetailCommandRepository paymentDetailCommandRepository;

    @Autowired
    public ApprovalCommandServiceImpl(ApprovalMappingCommandRepository approvalMappingCommandRepository,
                                      ApprovalCommandRepository approvalCommandRepository,
                                      ContractCommandRepository contractCommandRepository,
                                      PaymentDetailCommandRepository paymentDetailCommandRepository) {
        this.approvalMappingCommandRepository = approvalMappingCommandRepository;
        this.approvalCommandRepository = approvalCommandRepository;
        this.contractCommandRepository = contractCommandRepository;
        this.paymentDetailCommandRepository = paymentDetailCommandRepository;
    }

    @Override
    public void approve(Long approvalMappingId) {
        // 1) mapping 한 건 승인 처리
        ApprovalMappingCommandEntity mapping = getApprovalMapping(approvalMappingId);

        mapping.setIsApproved("Y");
        mapping.setRejectReason(null);

        ApprovalCommandEntity approval = mapping.getApproval();
        ContractCommandEntity contract = approval.getContract();

        Integer approvedStep = mapping.getStep();
        Long approvalId = approval.getId();

        // 2) 같은 approvalId 기준으로 "Y가 아닌 mapping"이 남아있는지 확인
        boolean hasNotApproved =
                approvalMappingCommandRepository.existsByApproval_IdAndIsApprovedNot(approvalId, "Y");

        if (!hasNotApproved) {
            // 전부 Y면: Approval 완료 처리 + Contract 활성화 + PaymentDetails 생성
            approval.setApprovalDate(LocalDateTime.now());
            approval.setStatus("A");
            contract.setStatus("P");
            insertPaymentDetailsForContract(contract);
        } else {
            // 아직 전부 Y가 아니면: current_step 업데이트
            contract.setCurrentStep(approvedStep);
        }
    }


    @Override
    public void reject(Long approvalMappingId, String rejectReason) {
        // 1. 승인 매핑 조회
        ApprovalMappingCommandEntity mapping = getApprovalMapping(approvalMappingId);

        // 이미 반려/승인된 건 방어
        if (!"U".equals(mapping.getIsApproved())) {
            throw new BusinessException(ErrorCode.APPROVAL_ALREADY_PROCESSED);
        }

        // 2. 매핑 반려 처리
        mapping.setIsApproved("N");
        mapping.setRejectReason(rejectReason);

        // 3. Approval / Contract 상태 전파
        ApprovalCommandEntity approval = mapping.getApproval();
        ContractCommandEntity contract = approval.getContract();

        approval.setStatus("R");
        contract.setStatus("R");
    }
    /**
     * 공통 조회 메서드
     */
    private ApprovalMappingCommandEntity getApprovalMapping(Long approvalMappingId) {
        return approvalMappingCommandRepository.findById(approvalMappingId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.APPROVAL_MAPPING_NOT_FOUND
                ));
    }

    private void insertPaymentDetailsForContract(ContractCommandEntity contract) {
        LocalDateTime startDate = contract.getStartDate();      // DATETIME
        Integer periodMonths = contract.getContractPeriod();    // 개월수

        if (startDate == null || periodMonths == null || periodMonths <= 0) {
            throw new BusinessException(ErrorCode.INVALID_CONTRACT_PERIOD);
        }

        List<PaymentDetailCommandEntity> rows = new ArrayList<>(periodMonths);

        for (int i = 0; i < periodMonths; i++) {
            rows.add(
                    PaymentDetailCommandEntity.builder()
                            .paymentDue(startDate.plusMonths(i))
                            .paymentActual(null)
                            .overdueDays(null)
                            .paymentStatus("P") // Pending
                            .contractId(contract.getId())
                            .build()
            );
        }

        paymentDetailCommandRepository.saveAll(rows);
    }
}
