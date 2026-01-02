package com.devoops.rentalbrain.customer.overdue.command.service;

import com.devoops.rentalbrain.business.contract.query.dto.PaymentOverdueCandidateDTO;
import com.devoops.rentalbrain.business.contract.query.service.PaymentDetailsQueryService;
import com.devoops.rentalbrain.customer.overdue.command.entity.PayOverdue;
import com.devoops.rentalbrain.customer.overdue.command.repository.PayOverdueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PayOverdueAutoCreateServiceImpl implements PayOverdueAutoCreateService {

    private final PaymentDetailsQueryService paymentQueryService;
    private final PayOverdueRepository payOverdueRepository;

    @Override
    public void createOverdueFromPaymentDetails() {

        List<PaymentOverdueCandidateDTO> candidates =
                paymentQueryService.findOverdueCandidates();

        for (PaymentOverdueCandidateDTO dto : candidates) {

            Long contractId = dto.getContractId();
            Long customerId = dto.getCustomerId();

            // 이미 미해결(P) 수납 연체가 있으면 생성하지 않음
            if (payOverdueRepository.existsByContractIdAndStatus(contractId, "P")) {
                continue;
            }

            PayOverdue payOverdue = PayOverdue.create(
                    contractId,
                    customerId,
                    dto.getOldestDueDate(),
                    dto.getOverdueCount()
            );

            payOverdueRepository.save(payOverdue);
        }
    }
}
