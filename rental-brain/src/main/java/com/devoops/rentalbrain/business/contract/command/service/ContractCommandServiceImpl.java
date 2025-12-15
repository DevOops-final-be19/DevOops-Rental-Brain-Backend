package com.devoops.rentalbrain.business.contract.command.service;

import com.devoops.rentalbrain.approval.command.Repository.ApprovalCommandRepository;
import com.devoops.rentalbrain.approval.command.Repository.ApprovalMappingCommandRepository;
import com.devoops.rentalbrain.approval.command.entity.ApprovalCommandEntity;
import com.devoops.rentalbrain.approval.command.entity.ApprovalMappingCommandEntity;
import com.devoops.rentalbrain.business.contract.command.dto.ContractCreateDTO;
import com.devoops.rentalbrain.business.contract.command.dto.ContractUpdateDTO;
import com.devoops.rentalbrain.business.contract.command.entity.ContractCommandEntity;
import com.devoops.rentalbrain.business.contract.command.repository.ContractCommandRepository;
import com.devoops.rentalbrain.common.codegenerator.CodeGenerator;
import com.devoops.rentalbrain.common.codegenerator.CodeType;
import com.devoops.rentalbrain.customer.customerlist.command.entity.CustomerlistCommandEntity;
import com.devoops.rentalbrain.employee.command.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ContractCommandServiceImpl implements ContractCommandService {

    private final ContractCommandRepository contractCommandRepository;
    private final ApprovalCommandRepository approvalCommandRepository;
    private final ApprovalMappingCommandRepository approvalMappingCommandRepository;
    private final ModelMapper modelMapper;
    private final CodeGenerator codeGenerator;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ContractCommandServiceImpl(
            ContractCommandRepository contractCommandRepository,
            ApprovalCommandRepository approvalCommandRepository,
            ApprovalMappingCommandRepository approvalMappingCommandRepository,
            ModelMapper modelMapper,
            CodeGenerator codeGenerator
    ) {
        this.contractCommandRepository = contractCommandRepository;
        this.approvalCommandRepository = approvalCommandRepository;
        this.approvalMappingCommandRepository = approvalMappingCommandRepository;
        this.modelMapper = modelMapper;
        this.codeGenerator = codeGenerator;
    }

    /**
     * 계약 상태 자동 변경 스케줄러
     *
     * 상태 흐름:
     * P(진행중) → I(만료임박, 1개월 전) → C(계약만료)
     */
    @Override
    @Scheduled(cron = "0 0 1 * * *") // 매일 새벽 1시
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
        // 계약생성

        // DTO → Entity 매핑
        ContractCommandEntity contract = new ContractCommandEntity();
        contract.setName(dto.getContractName());
        contract.setStartDate(dto.getStartDate());
        contract.setContractPeriod(dto.getContractPeriod());
        contract.setMonthlyPayment(dto.getMonthlyPayment());
        contract.setTotalAmount(dto.getTotalAmount());
        contract.setPayMethod(dto.getPayMethod());
        contract.setSpecialContent(dto.getSpecialContent());

        // 상태값 세팅
        contract.setStatus("W");       // 결제대기
        contract.setCurrentStep(1);    // 승인 1단계 시작

        // 계약 코드 생성
        contract.setContractCode(
                codeGenerator.generate(CodeType.CONTRACT)
        );

        CustomerlistCommandEntity customerRef =
                entityManager.getReference(
                        CustomerlistCommandEntity.class,
                        dto.getCumId()
                );
        contract.setCustomer(customerRef);

        // 저장
        ContractCommandEntity savedContract =
                contractCommandRepository.save(contract);


        // 결제생성
        ApprovalCommandEntity approval = new ApprovalCommandEntity();

        approval.setApprovalCode(
                codeGenerator.generate(CodeType.APPROVAL)
        );

        approval.setTitle(dto.getContractName());
        approval.setRequestDate(dto.getStartDate());
        approval.setStatus("P"); // 승인 대기
        approval.setContract(savedContract);

        Employee employeeRef =
                entityManager.getReference(Employee.class, dto.getCumId());
        approval.setEmployee(employeeRef);

        ApprovalCommandEntity savedApproval =
                approvalCommandRepository.save(approval);

        createApprovalMapping(
                savedApproval,
                dto.getMemId(),
                dto.getLeaderId(),
                dto.getCeoId()
        );
    }


    @Override
    public void updateContract(ContractUpdateDTO dto) {

    }


    private void createApprovalMapping(
            ApprovalCommandEntity approval,
            Long memId,
            Long leaderId,
            Long ceoId
    ) {
        Employee memRef =
                entityManager.getReference(Employee.class, memId);
        Employee leaderRef =
                entityManager.getReference(Employee.class, leaderId);
        Employee ceoRef =
                entityManager.getReference(Employee.class, ceoId);

        ApprovalMappingCommandEntity step1 =
                ApprovalMappingCommandEntity.builder()
                        .approval(approval)
                        .employee(memRef)
                        .step(1)
                        .isApproved("U")
                        .build();

        ApprovalMappingCommandEntity step2 =
                ApprovalMappingCommandEntity.builder()
                        .approval(approval)
                        .employee(leaderRef)
                        .step(2)
                        .isApproved("U")
                        .build();

        ApprovalMappingCommandEntity step3 =
                ApprovalMappingCommandEntity.builder()
                        .approval(approval)
                        .employee(ceoRef)
                        .step(3)
                        .isApproved("U")
                        .build();

        approvalMappingCommandRepository.saveAll(
                List.of(step1, step2, step3)
        );
    }



}
