package com.devoops.rentalbrain.business.contract.command.service;

import com.devoops.rentalbrain.approval.command.Repository.ApprovalCommandRepository;
import com.devoops.rentalbrain.approval.command.Repository.ApprovalMappingCommandRepository;
import com.devoops.rentalbrain.approval.command.entity.ApprovalCommandEntity;
import com.devoops.rentalbrain.approval.command.entity.ApprovalMappingCommandEntity;
import com.devoops.rentalbrain.business.contract.command.dto.ContractCreateDTO;
import com.devoops.rentalbrain.business.contract.command.dto.ContractItemDTO;
import com.devoops.rentalbrain.business.contract.command.dto.ContractUpdateDTO;
import com.devoops.rentalbrain.business.contract.command.entity.ContractCommandEntity;
import com.devoops.rentalbrain.business.contract.command.entity.ContractItemCommandEntity;
import com.devoops.rentalbrain.business.contract.command.entity.PaymentDetailCommandEntity;
import com.devoops.rentalbrain.business.contract.command.repository.ContractCommandRepository;
import com.devoops.rentalbrain.business.contract.command.repository.ContractItemCommandRepository;
import com.devoops.rentalbrain.business.contract.command.repository.PaymentDetailCommandRepository;
import com.devoops.rentalbrain.common.codegenerator.CodeGenerator;
import com.devoops.rentalbrain.common.codegenerator.CodeType;
import com.devoops.rentalbrain.common.error.ErrorCode;
import com.devoops.rentalbrain.common.error.exception.BusinessException;
import com.devoops.rentalbrain.common.segmentrebuild.command.service.SegmentTransitionCommandService;
import com.devoops.rentalbrain.customer.customerlist.command.entity.CustomerlistCommandEntity;
import com.devoops.rentalbrain.employee.command.dto.UserImpl;
import com.devoops.rentalbrain.employee.command.entity.Employee;
import com.devoops.rentalbrain.product.productlist.command.repository.ItemRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ContractCommandServiceImpl implements ContractCommandService {

    private final ContractCommandRepository contractCommandRepository;
    private final ApprovalCommandRepository approvalCommandRepository;
    private final ItemRepository itemRepository;
    private final ContractItemCommandRepository contractItemCommandRepository;
    private final ApprovalMappingCommandRepository approvalMappingCommandRepository;
    private final PaymentDetailCommandRepository paymentDetailCommandRepository;
    private final CodeGenerator codeGenerator;
    private final SegmentTransitionCommandService segmentTransitionCommandService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ContractCommandServiceImpl(
            ContractCommandRepository contractCommandRepository,
            ApprovalCommandRepository approvalCommandRepository,
            ItemRepository itemRepository,
            ContractItemCommandRepository contractItemCommandRepository,
            ApprovalMappingCommandRepository approvalMappingCommandRepository,
            PaymentDetailCommandRepository paymentDetailCommandRepository,
            CodeGenerator codeGenerator,
            SegmentTransitionCommandService segmentTransitionCommandService
    ) {
        this.contractCommandRepository = contractCommandRepository;
        this.approvalCommandRepository = approvalCommandRepository;
        this.itemRepository = itemRepository;
        this.contractItemCommandRepository = contractItemCommandRepository;
        this.approvalMappingCommandRepository = approvalMappingCommandRepository;
        this.paymentDetailCommandRepository = paymentDetailCommandRepository;
        this.codeGenerator = codeGenerator;
        this.segmentTransitionCommandService = segmentTransitionCommandService;
    }

    /**
     * 계약 상태 자동 변경 스케줄러
     *
     * 상태 흐름:
     * P(진행중) → I(만료임박, 1개월 전) → C(계약만료)
     */
    @Override
    @Scheduled(cron = "0 0 6 * * *")
    @Transactional
    public void updateContractStatus() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMonthLater = now.plusMonths(1);

        int imminent =
                contractCommandRepository.updateToExpireImminent(
                        now, oneMonthLater
                );

        int closed =
                contractCommandRepository.updateToClosed(now);

        log.info(
                "[계약 상태 스케줄러] 만료임박(I): {}, 계약만료(C): {}",
                imminent, closed
        );
    }

    @Override
    @Transactional
    public void createContract(ContractCreateDTO dto) {

        /* =====================
       0. 로그인 사용자 검증
       ===================== */
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserImpl user)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        Long loginEmpId;
        try {
            loginEmpId = user.getId();
        } catch (NumberFormatException e) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        Employee loginEmp = entityManager.find(Employee.class, loginEmpId);
        if (loginEmp == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        int positionId = Math.toIntExact(loginEmp.getPositionId());

         /* =====================
       1. 요청 기반 역할 판단
       ===================== */

        Long leaderId = dto.getLeaderId();
        Long ceoId = dto.getCeoId();

        // 대표
        boolean isCeoRequest = leaderId == null && ceoId == null;

        // 팀장
        boolean isLeaderRequest = leaderId == null && ceoId != null;

        // 팀원
        boolean isMemberRequest = leaderId != null && ceoId != null;

        /* =====================
       2. 역할 + 직급 검증
       ===================== */

        // 대표
        if (isCeoRequest) {
            if (positionId != 1) {
                throw new BusinessException(
                        ErrorCode.FORBIDDEN,
                        "대표만 결재선 없이 계약을 생성할 수 있습니다."
                );
            }
        }

        // 팀장
        else if (isLeaderRequest) {
            if (!List.of(2, 3, 4).contains(positionId)) {
                throw new BusinessException(
                        ErrorCode.FORBIDDEN,
                        "팀장만 CEO 결재 계약을 생성할 수 있습니다."
                );
            }
        }

        // 팀원
        else if (isMemberRequest) {
            if (!List.of(5, 6, 7).contains(positionId)) {
                throw new BusinessException(
                        ErrorCode.FORBIDDEN,
                        "팀원만 팀장 + CEO 결재 계약을 생성할 수 있습니다."
                );
            }
        }

        else {
            throw new BusinessException(
                    ErrorCode.CONTRACT_INVALID_APPROVAL_REQUEST,
                    "잘못된 결재선 요청입니다."
            );
        }
        /* =====================
            2. 계약 생성
       ===================== */

        // DTO → Entity 매핑
        ContractCommandEntity contract = new ContractCommandEntity();
        contract.setName(dto.getContractName());
        contract.setStartDate(dto.getStartDate());
        contract.setContractPeriod(dto.getContractPeriod());
        contract.setMonthlyPayment(dto.getMonthlyPayment());
        contract.setTotalAmount(dto.getTotalAmount());
        contract.setPayMethod(dto.getPayMethod());
        contract.setSpecialContent(dto.getSpecialContent());

        // 계약 코드 생성
        contract.setContractCode(
                codeGenerator.generate(CodeType.CONTRACT)
        );

        // 상태값 세팅
        contract.setStatus(isCeoRequest ? "P" : "W");
        contract.setCurrentStep(1);

        CustomerlistCommandEntity customerRef =
                entityManager.getReference(
                        CustomerlistCommandEntity.class,
                        dto.getCumId()
                );
        contract.setCustomer(customerRef);

        // 저장
        ContractCommandEntity savedContract =
                contractCommandRepository.save(contract);


        /* =====================
             4. 승인 생성
       ===================== */

        ApprovalCommandEntity approval = new ApprovalCommandEntity();

        approval.setApprovalCode(
                codeGenerator.generate(CodeType.APPROVAL)
        );

        approval.setTitle(dto.getContractName());
        approval.setRequestDate(LocalDateTime.now());
        approval.setStatus("P"); // 승인 대기
        approval.setContract(savedContract);

        approval.setEmployee(
                entityManager.getReference(Employee.class, loginEmpId)
        );

        ApprovalCommandEntity savedApproval =
                approvalCommandRepository.save(approval);

        createApprovalMapping(
                savedApproval,
                leaderId,
                ceoId,
                loginEmpId
        );

        /* =====================
       3. 제품 상태 update + 계약-제품 매핑 insert
       ===================== */
        for (ContractItemDTO item : dto.getItems()) {

            // 1) 대여 가능한 item id 조회
            List<Long> itemIds =
                    itemRepository.findRentableItemIdsByName(
                            item.getItemName(),
                            item.getQuantity()
                    );

            int requested = item.getQuantity();   // 요청 수량
            int available = itemIds.size();       // 실제 가능 수량
            int shortage  = requested - available;

            if (itemIds.size() < item.getQuantity()) {
                throw new BusinessException(
                        ErrorCode.CONTRACT_ITEM_STOCK_NOT_ENOUGH,
                        String.format(
                                "상품 [%s] 재고 부족 - 요청: %d, 가능: %d, 부족: %d",
                                item.getItemName(),
                                requested,
                                available,
                                shortage
                        )
                );
            }

            // 2) item 상태 변경 (P → S)
            itemRepository.updateItemStatusToRented(itemIds);

            // 3) contract_with_item insert
            List<ContractItemCommandEntity> mappings =
                    itemIds.stream()
                            .map(itemId -> {
                                ContractItemCommandEntity e = new ContractItemCommandEntity();
                                e.setContractId(savedContract.getId());
                                e.setItemId(itemId);
                                return e;
                            })
                            .toList();

            contractItemCommandRepository.saveAll(mappings);
        }

        // 세그먼트 트리거 추가

        Long customerId = dto.getCumId();
        Long contractId = savedContract.getId(); // save 이후라 반드시 존재

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {

                    @Override
                    public void afterCommit() {
                        try {
                            segmentTransitionCommandService.onContractCommitted(customerId, contractId);
                        } catch (Exception e) {
                            // afterCommit은 rollback 불가 → 로그만
                            log.error(
                                    "[SEGMENT][AFTER_COMMIT_FAIL] customerId={}, contractId={}",
                                    customerId,
                                    contractId,
                                    e
                            );
                        }
                    }
                }
        );
    }


    @Override
    public void updateContract(ContractUpdateDTO dto) {

    }


    private void createApprovalMapping(
            ApprovalCommandEntity approval,
            Long leaderId,
            Long ceoId,
            Long loginEmpId
    ) {
        ContractCommandEntity contract = approval.getContract();

        // case 1: CEO
        if (leaderId == null && ceoId == null) {

            ApprovalMappingCommandEntity ceoStep =
                    ApprovalMappingCommandEntity.builder()
                            .approval(approval)
                            .employee(entityManager.getReference(Employee.class, loginEmpId))
                            .step(3)
                            .isApproved("Y")
                            .build();

            approvalMappingCommandRepository.save(ceoStep);

            approval.setApprovalDate(LocalDateTime.now());
            approval.setStatus("A");

            contract.setCurrentStep(3);
            contract.setStatus("P"); // 계약 진행

            insertPaymentDetailsForContract(contract);
            return;
        }

        // case 2: 팀장
        if (leaderId == null && ceoId != null) {

            ApprovalMappingCommandEntity leaderStep =
                    ApprovalMappingCommandEntity.builder()
                            .approval(approval)
                            .employee(entityManager.getReference(Employee.class, loginEmpId))
                            .step(2)
                            .isApproved("Y")
                            .build();

            ApprovalMappingCommandEntity ceoStep =
                    ApprovalMappingCommandEntity.builder()
                            .approval(approval)
                            .employee(entityManager.getReference(Employee.class, ceoId))
                            .step(3)
                            .isApproved("U")
                            .build();

            approvalMappingCommandRepository.saveAll(
                    List.of(leaderStep, ceoStep)
            );

            contract.setCurrentStep(2);
            contract.setStatus("W");
            return;
        }

        // case 3: mem + leader + ceo (기본)
        Employee leaderRef =
                entityManager.getReference(Employee.class, leaderId);
        Employee ceoRef =
                entityManager.getReference(Employee.class, ceoId);

        ApprovalMappingCommandEntity leaderStep =
                ApprovalMappingCommandEntity.builder()
                        .approval(approval)
                        .employee(leaderRef)
                        .step(2)
                        .isApproved("U")
                        .build();

        ApprovalMappingCommandEntity ceoStep =
                ApprovalMappingCommandEntity.builder()
                        .approval(approval)
                        .employee(ceoRef)
                        .step(3)
                        .isApproved("U")
                        .build();

        approvalMappingCommandRepository.saveAll(
                List.of(leaderStep, ceoStep)
        );

        contract.setCurrentStep(1);
        contract.setStatus("W");
    }

    private void insertPaymentDetailsForContract(ContractCommandEntity contract) {

        LocalDateTime startDate = contract.getStartDate();
        Integer periodMonths = contract.getContractPeriod();

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
                            .paymentStatus("P")
                            .contractId(contract.getId())
                            .build()
            );
        }

        paymentDetailCommandRepository.saveAll(rows);
    }

}
