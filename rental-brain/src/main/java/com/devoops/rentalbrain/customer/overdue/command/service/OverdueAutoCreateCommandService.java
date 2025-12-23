package com.devoops.rentalbrain.customer.overdue.command.service;

import com.devoops.rentalbrain.business.contract.query.dto.PaymentOverdueCandidateDTO;
import com.devoops.rentalbrain.business.contract.query.service.PaymentDetailsQueryService;
import com.devoops.rentalbrain.customer.overdue.command.entity.ItemOverdue;
import com.devoops.rentalbrain.customer.overdue.command.entity.PayOverdue;
import com.devoops.rentalbrain.customer.overdue.command.repository.ItemOverdueRepository;
import com.devoops.rentalbrain.customer.overdue.command.repository.PayOverdueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OverdueAutoCreateCommandService {

    private final PaymentDetailsQueryService paymentQueryService;
    private final PayOverdueRepository payOverdueRepository;
    private final ItemOverdueRepository itemOverdueRepository;

    public void createOverdueFromPaymentDetails() {

        List<PaymentOverdueCandidateDTO> candidates =
                paymentQueryService.findOverdueCandidates();

        for (PaymentOverdueCandidateDTO dto : candidates) {

            Long contractId = dto.getContractId();
            Long customerId = dto.getCustomerId();

            // 수납 연체
            if (!payOverdueRepository.existsByContractIdAndStatus(contractId, "P")) {

                PayOverdue payOverdue = PayOverdue.create(
                        contractId,
                        customerId,
                        dto.getOldestDueDate(),
                        dto.getOverdueCount()
                );
                payOverdueRepository.save(payOverdue);
            }

            // 제품 연체
            if (!itemOverdueRepository.existsByContractIdAndStatus(contractId, "P")) {

                ItemOverdue itemOverdue = ItemOverdue.create(
                        contractId,
                        customerId,
                        dto.getOverdueCount()
                );
                itemOverdueRepository.save(itemOverdue);
            }
        }
    }
}
