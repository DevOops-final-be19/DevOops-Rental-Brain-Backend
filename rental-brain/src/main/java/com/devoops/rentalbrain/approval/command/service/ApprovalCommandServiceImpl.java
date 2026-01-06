package com.devoops.rentalbrain.approval.command.service;



import com.devoops.rentalbrain.approval.command.Repository.ApprovalMappingCommandRepository;
import com.devoops.rentalbrain.approval.command.entity.ApprovalCommandEntity;
import com.devoops.rentalbrain.approval.command.entity.ApprovalMappingCommandEntity;
import com.devoops.rentalbrain.business.campaign.command.service.CouponCommandService;
import com.devoops.rentalbrain.business.contract.command.entity.ContractCommandEntity;
import com.devoops.rentalbrain.business.contract.command.entity.PaymentDetailCommandEntity;
import com.devoops.rentalbrain.business.contract.command.repository.PaymentDetailCommandRepository;
import com.devoops.rentalbrain.common.error.ErrorCode;
import com.devoops.rentalbrain.common.error.exception.BusinessException;
import com.devoops.rentalbrain.common.notice.application.domain.PositionType;
import com.devoops.rentalbrain.common.notice.application.facade.NotificationPublisher;
import com.devoops.rentalbrain.common.notice.application.strategy.event.ApprovalRequestEvent;
import com.devoops.rentalbrain.common.notice.application.strategy.event.ContractApprovedEvent;
import com.devoops.rentalbrain.common.notice.application.strategy.event.QuoteInsertedEvent;
import com.devoops.rentalbrain.employee.command.dto.UserImpl;
import com.devoops.rentalbrain.product.productlist.command.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class ApprovalCommandServiceImpl implements ApprovalCommandService {

    private final ApprovalMappingCommandRepository approvalMappingCommandRepository;
    private final PaymentDetailCommandRepository paymentDetailCommandRepository;
    private final ItemRepository itemRepository;
    private final CouponCommandService couponCommandService;
    private final NotificationPublisher notificationPublisher;

    @Autowired
    public ApprovalCommandServiceImpl(ApprovalMappingCommandRepository approvalMappingCommandRepository,
                                      PaymentDetailCommandRepository paymentDetailCommandRepository,
                                      ItemRepository itemRepository,
                                      CouponCommandService couponCommandService,
                                      NotificationPublisher notificationPublisher) {
        this.approvalMappingCommandRepository = approvalMappingCommandRepository;
        this.paymentDetailCommandRepository = paymentDetailCommandRepository;
        this.itemRepository = itemRepository;
        this.couponCommandService = couponCommandService;
        this.notificationPublisher = notificationPublisher;
    }

    @Override
    public void approve(Long approvalMappingId) {

        // 1) mapping 한 건 승인 처리
        ApprovalMappingCommandEntity mapping = getApprovalMapping(approvalMappingId);

        // 2) 권한 및 단계 검증
        validateApprovalAuthority(mapping);

        // 3) 이미 승인/반려된건
        if (!"U".equals(mapping.getIsApproved())) {
            throw new BusinessException(ErrorCode.APPROVAL_ALREADY_PROCESSED);
        }

        // 3. 이미 반려/승인된 건 방어
        mapping.setIsApproved("Y");
        mapping.setRejectReason(null);

        ApprovalCommandEntity approval = mapping.getApproval();
        ContractCommandEntity contract = approval.getContract();

        Integer approvedStep = mapping.getStep();
        Long approvalId = approval.getId();

        // 4. 같은 approvalId 기준으로 미승인 mapping 존재 여부 확인
        boolean hasNotApproved =
                approvalMappingCommandRepository.existsByApproval_IdAndIsApprovedNot(approvalId, "Y");

        if (!hasNotApproved) {
            // 전부 Y면: Approval 완료 처리 + Contract 활성화 + PaymentDetails 생성
            approval.setApprovalDate(LocalDateTime.now());
            approval.setStatus("A");
            contract.setStatus("P");
            insertPaymentDetailsForContract(contract);
            couponCommandService.updateIssuedCoupon(contract.getId());
            notificationPublisher.publish(new ContractApprovedEvent(approval.getEmployee().getId(),'A'));
        } else {
            // 아직 전부 Y가 아니면: current_step 업데이트
            contract.setCurrentStep(approvedStep);
            ApprovalMappingCommandEntity approvalMappingCommandEntity = approvalMappingCommandRepository.findByApprovalIdAndIsApproved(approval.getId(), "U");
            notificationPublisher.publish(new ApprovalRequestEvent(approvalMappingCommandEntity.getEmployee().getId(),approval.getTitle()));
        }
    }


    @Override
    public void reject(Long approvalMappingId, String rejectReason) {

        // 1. 승인 매핑 조회
        ApprovalMappingCommandEntity mapping = getApprovalMapping(approvalMappingId);

        // 2. 권한 및 단계 검증
        validateApprovalAuthority(mapping);

        // 3. 이미 반려/승인된 건 방어
        if (!"U".equals(mapping.getIsApproved())) {
            throw new BusinessException(ErrorCode.APPROVAL_ALREADY_PROCESSED);
        }

        // 4. 매핑 반려 처리
        mapping.setIsApproved("N");
        mapping.setRejectReason(rejectReason);

        // 5. 상태 전파
        ApprovalCommandEntity approval = mapping.getApproval();
        ContractCommandEntity contract = approval.getContract();

        approval.setStatus("R");
        contract.setStatus("R");

        // 6. 결재 반려 → 아이템 상태 롤백
        rollbackContractItems(contract.getId());

        notificationPublisher.publish(new ContractApprovedEvent(approval.getEmployee().getId(),'R'));

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
                            .overdueDays(0)
                            .paymentStatus("P") // Pending
                            .contractId(contract.getId())
                            .build()
            );
        }

        paymentDetailCommandRepository.saveAll(rows);
    }

    private Long getCurrentEmpId() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserImpl user)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        return Long.valueOf(user.getId());
    }

    private void validateApprovalAuthority(ApprovalMappingCommandEntity mapping) {

        Long currentEmpId = getCurrentEmpId();

        // 내 결재인지 확인
        if (!mapping.getEmployee().getId().equals(currentEmpId)) {
            throw new BusinessException(ErrorCode.APPROVAL_ACCESS_DENIED);
        }

        // 이미 처리된 건 방어
        if (!"U".equals(mapping.getIsApproved())) {
            throw new BusinessException(ErrorCode.APPROVAL_ALREADY_PROCESSED);
        }

        Long approvalId = mapping.getApproval().getId();
        Integer step = mapping.getStep();

        // 이전 단계 완료 여부 확인
        boolean hasPrevNotApproved =
                approvalMappingCommandRepository
                        .existsByApproval_IdAndStepLessThanAndIsApprovedNot(
                                approvalId,
                                step,
                                "Y"
                        );

        if (hasPrevNotApproved) {
            throw new BusinessException(
                    ErrorCode.APPROVAL_PREVIOUS_STEP_NOT_COMPLETED
            );
        }
    }

    private void rollbackContractItems(Long contractId) {

        int rolledBackCount =
                itemRepository.rollbackItemsToPending(contractId);

        if (rolledBackCount == 0) {
            // 데이터 이상 추적용 로그 (예외까지는 X)
            log.warn(
                    "[Approval Reject] No items rolled back. contractId={}",
                    contractId
            );
        }
    }
}
