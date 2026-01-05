package com.devoops.rentalbrain.business.contract.command.service;

import com.devoops.rentalbrain.business.contract.command.dto.PaymentDetailRequestDTO;
import com.devoops.rentalbrain.business.contract.command.entity.PaymentDetailCommandEntity;
import com.devoops.rentalbrain.business.contract.command.repository.PaymentDetailCommandRepository;
import com.devoops.rentalbrain.product.productlist.command.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
public class PaymentDetailCommandServiceImpl implements PaymentDetailCommandService {

    private final PaymentDetailCommandRepository paymentDetailCommandRepository;
    private final ItemRepository itemRepository;


    @Autowired
    public PaymentDetailCommandServiceImpl(PaymentDetailCommandRepository paymentDetailCommandRepository,
                                           ItemRepository itemRepository) {
        this.paymentDetailCommandRepository = paymentDetailCommandRepository;
        this.itemRepository = itemRepository;
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
        paymentDetail.setOverdueDays(0);

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
    public void autoMarkAsNonPayment() {
        LocalDateTime now = LocalDateTime.now();

        int updated = paymentDetailCommandRepository.markAsNonPayment(now);

        log.warn("[AUTO NON-PAYMENT] {}건 미납 전환 처리", updated);

    }

    @Override
    public void increaseOverdueDays() {
        int updated = paymentDetailCommandRepository.increaseOverdueDaysForNonPayment();
        log.info("[Scheduler] 미납 연체일수 증가 완료 - {}건", updated);
    }
}
