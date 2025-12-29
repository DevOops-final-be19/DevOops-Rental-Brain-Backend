package com.devoops.rentalbrain.business.contract.command.service;

import com.devoops.rentalbrain.business.contract.command.dto.PaymentDetailRequestDTO;
import com.devoops.rentalbrain.business.contract.command.entity.ContractCommandEntity;
import com.devoops.rentalbrain.business.contract.command.entity.PaymentDetailCommandEntity;
import com.devoops.rentalbrain.business.contract.command.repository.ContractCommandRepository;
import com.devoops.rentalbrain.business.contract.command.repository.PaymentDetailCommandRepository;
import com.devoops.rentalbrain.common.codegenerator.CodeGenerator;
import com.devoops.rentalbrain.common.codegenerator.CodeType;
import com.devoops.rentalbrain.customer.overdue.command.entity.PayOverdue;
import com.devoops.rentalbrain.customer.overdue.command.repository.PayOverdueRepository;
import com.devoops.rentalbrain.product.productlist.command.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@Transactional
public class PaymentDetailCommandServiceImpl implements PaymentDetailCommandService {

    private final PaymentDetailCommandRepository paymentDetailCommandRepository;
    private final ItemRepository itemRepository;
    private final PayOverdueRepository payOverdueRepository;
    private final ContractCommandRepository contractCommandRepository;
    private final CodeGenerator codeGenerator;

    @Autowired
    public PaymentDetailCommandServiceImpl(PaymentDetailCommandRepository paymentDetailCommandRepository,
                                           ItemRepository itemRepository,
                                           PayOverdueRepository payOverdueRepository,
                                           ContractCommandRepository contractCommandRepository,
                                           CodeGenerator codeGenerator) {
        this.paymentDetailCommandRepository = paymentDetailCommandRepository;
        this.itemRepository = itemRepository;
        this.payOverdueRepository = payOverdueRepository;
        this.contractCommandRepository = contractCommandRepository;
        this.codeGenerator = codeGenerator;
    }

    @Override
    public void completePayment(Long paymentDetailId, PaymentDetailRequestDTO dto){

        PaymentDetailCommandEntity paymentDetail =
                paymentDetailCommandRepository.findById(paymentDetailId)
                        .orElseThrow(() ->
                                new EntityNotFoundException("결재 내역이 존재하지 않습니다. id=" + paymentDetailId));
        // 중복 처리 방지 (아주 중요)
        if ("C".equals(paymentDetail.getPaymentStatus())) {
            throw new IllegalStateException("이미 완료된 결제입니다.");
        }

        // 1. 결제 완료 처리
        paymentDetail.setPaymentActual(dto.getPaymentActual());
        paymentDetail.setPaymentStatus("C");

        // 2. 계약 ID 추출
        Long contractId = paymentDetail.getContractId();

        // 3. 아이템 매출 누적
        int updated = itemRepository.addMonthlySalesByContract(contractId);

        log.info(
                "[Payment Complete] contractId={}, updatedItems={}",
                contractId, updated
        );
    }

    @Override
    public void markAsNonPayment(Long paymentDetailId) {
        PaymentDetailCommandEntity paymentDetail =
                paymentDetailCommandRepository.findById(paymentDetailId)
                        .orElseThrow(() ->
                                new EntityNotFoundException("결재 내역이 존재하지 않습니다. id=" + paymentDetailId));

        // 이미 완료된 결제는 미납으로 변경 불가
        if ("C".equals(paymentDetail.getPaymentStatus())) {
            throw new IllegalStateException("완료된 결제는 미납 상태로 변경할 수 없습니다.");
        }

        // 납부일이 있는데 미납으로 바꾸는 것도 방지
        if (paymentDetail.getPaymentActual() != null) {
            throw new IllegalStateException("실제 납부일이 존재하는 결제는 미납 처리할 수 없습니다.");
        }

        LocalDateTime now = LocalDateTime.now();

        // 예정일 이전이면 미납 처리 불가
        if (now.isBefore(paymentDetail.getPaymentDue())) {
            throw new IllegalStateException("결제 예정일 이전에는 미납 처리할 수 없습니다.");
        }

        // 연체 일수 계산
        long overdueDays = ChronoUnit.DAYS.between(
                paymentDetail.getPaymentDue().toLocalDate(),
                now.toLocalDate()
        );

        paymentDetail.setPaymentStatus("N");
        paymentDetail.setOverdueDays((int) overdueDays);

        ContractCommandEntity contract =
                contractCommandRepository.findById(paymentDetail.getContractId())
                        .orElseThrow(() ->
                                new EntityNotFoundException("계약이 존재하지 않습니다."));

        PayOverdue overdue = PayOverdue.create(
                paymentDetail.getContractId(),
                contract.getCustomer().getId(),
                paymentDetail.getPaymentDue(),
                (int) overdueDays
        );
        overdue.setPayOverdueCode(
                codeGenerator.generate(CodeType.PAY_OVERDUE)
        );

        payOverdueRepository.save(overdue);

        log.info("미납 처리 완료 - id={}, overdueDays={}", paymentDetailId, overdueDays);
    }
    @Override
    public void increaseOverdueDays() {
        int updated = paymentDetailCommandRepository.increaseOverdueDaysForNonPayment();
        log.info("[Scheduler] 미납 연체일수 증가 완료 - {}건", updated);
    }
}
