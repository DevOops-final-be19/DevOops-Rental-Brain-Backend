package com.devoops.rentalbrain.common.segmentrebuild.command.service;

import com.devoops.rentalbrain.customer.customerlist.command.entity.CustomerlistCommandEntity;
import com.devoops.rentalbrain.customer.customerlist.command.repository.CustomerlistCommandRepository;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.domain.SegmentChangeReferenceType;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.domain.SegmentChangeTriggerType;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.entity.HistoryCommandEntity;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.repository.HistoryCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SegmentTransitionCommandServiceImpl implements SegmentTransitionCommandService {

    private final CustomerlistCommandRepository customerlistCommandRepository;
    private final HistoryCommandRepository historyCommandRepository;

    private static final long SEG_POTENTIAL = 1L; // 잠재
    private static final long SEG_NEW       = 2L; // 신규

    // 계약 이벤트: 잠재 -> 신규
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onContractCommitted(Long customerId, Long contractId) {

        CustomerlistCommandEntity customer = customerlistCommandRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("고객을 찾을 수 없습니다. : " + customerId));

        Long currentSegmentId = customer.getSegmentId();

        if (currentSegmentId != null && currentSegmentId.equals(SEG_POTENTIAL)) {
            customer.setSegmentId(SEG_NEW);
            customerlistCommandRepository.saveAndFlush(customer);

            historyCommandRepository.save(
                    HistoryCommandEntity.builder()
                            .customerId(customerId)
                            .previousSegmentId(SEG_POTENTIAL)
                            .currentSegmentId(SEG_NEW)
                            .reason("첫 계약 체결")
                            .triggerType(SegmentChangeTriggerType.AUTO)
                            .referenceType(SegmentChangeReferenceType.CONTRACT)
                            .referenceId(contractId)
                            .build()
            );

            log.info("[세그먼트][AUTO] customerId={} 잠재(1) → 신규(2) (contractId={})", customerId, contractId);
        }
    }
}
