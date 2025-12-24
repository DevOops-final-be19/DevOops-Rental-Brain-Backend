package com.devoops.rentalbrain.common.segmentrebuild.command.service;

import com.devoops.rentalbrain.common.segmentrebuild.command.mapper.SegmentTransitionCommandMapper;
import com.devoops.rentalbrain.customer.customerlist.command.entity.CustomerlistCommandEntity;
import com.devoops.rentalbrain.customer.customerlist.command.repository.CustomerlistCommandRepository;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.domain.SegmentChangeReferenceType;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.domain.SegmentChangeTriggerType;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.entity.HistoryCommandEntity;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.repository.HistoryCommandRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SegmentTransitionCommandServiceImpl implements SegmentTransitionCommandService {

    private final SegmentTransitionCommandMapper segmentTransitionCommandMapper;

    private static final long SEG_POTENTIAL = 1L; // 잠재
    private static final long SEG_NEW       = 2L; // 신규

    private final CustomerlistCommandRepository customerlistCommandRepository;
    private final HistoryCommandRepository historyCommandRepository;

    // 잠재 -> 신규 고객 (segmentId 1 -> 2)
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onContractCommitted(Long customerId, Long contractId) {

        CustomerlistCommandEntity customer = customerlistCommandRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("고객을 찾을 수 없습니다. : " + customerId));

        Long currentSegmentId = customer.getSegmentId();

        // 잠재 고객일 때만 신규로 승격
        if (currentSegmentId != null && currentSegmentId == SEG_POTENTIAL) {
            customer.setSegmentId(SEG_NEW); // dirty checking으로 UPDATE

            customerlistCommandRepository.saveAndFlush(customer); // flush 물 내려버리기

            historyCommandRepository.save(
                    HistoryCommandEntity.builder()
                            .customerId(customerId)
                            .previousSegmentId(SEG_POTENTIAL)
                            .currentSegmentId(SEG_NEW)
                            .reason("첫 계약 체결")
                            .triggerType(SegmentChangeTriggerType.valueOf("AUTO"))
                            .referenceType(SegmentChangeReferenceType.valueOf("CONTRACT"))
                            .referenceId(contractId)
                            .build()
            );

            log.info("[SEGMENT] customerId={} POTENTIAL(1) -> NEW(2) (contractId={})",
                    customerId, contractId);
        } else {
            log.info("[SEGMENT] customerId={} skip (currentSegmentId={})", customerId, currentSegmentId);
        }

    }
}
