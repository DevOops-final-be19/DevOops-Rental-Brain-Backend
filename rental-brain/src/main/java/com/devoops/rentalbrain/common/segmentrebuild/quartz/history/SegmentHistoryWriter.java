package com.devoops.rentalbrain.common.segmentrebuild.quartz.history;


import com.devoops.rentalbrain.customer.customersegmenthistory.command.domain.SegmentChangeReferenceType;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.domain.SegmentChangeTriggerType;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.entity.HistoryCommandEntity;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.repository.HistoryCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SegmentHistoryWriter {

    private final HistoryCommandRepository historyCommandRepository;



    @Transactional
    public HistoryCommandEntity save(Long customerId,
                                     Long previousSegmentId,
                                     Long currentSegmentId,
                                     String reason,
                                     SegmentChangeTriggerType triggerType,
                                     SegmentChangeReferenceType referenceType,
                                     Long referenceId) {

        HistoryCommandEntity entity = HistoryCommandEntity.builder()
                .customerId(customerId)
                .previousSegmentId(previousSegmentId)
                .currentSegmentId(currentSegmentId)
                .reason(reason)
                .triggerType(triggerType)
                .referenceType(referenceType)
                .referenceId(referenceId)
                // changedAt은 @PrePersist에서 자동 세팅됨
                .build();

        return historyCommandRepository.save(entity);
    }
}
