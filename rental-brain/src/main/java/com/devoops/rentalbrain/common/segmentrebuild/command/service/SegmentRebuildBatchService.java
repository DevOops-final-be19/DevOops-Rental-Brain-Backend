package com.devoops.rentalbrain.common.segmentrebuild.command.service;

import com.devoops.rentalbrain.common.segmentrebuild.command.repository.SegmentRebuildBatchRepository;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.domain.SegmentChangeReferenceType;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.domain.SegmentChangeTriggerType;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.entity.HistoryCommandEntity;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.repository.HistoryCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SegmentRebuildBatchService {

    private final SegmentRebuildBatchRepository segmentRebuildBatchRepository;
    private final HistoryCommandRepository historyCommandRepository;

    private static final long SEG_POTENTIAL = 1L;
    private static final long SEG_NEW       = 2L;
    private static final long SEG_NORMAL    = 3L;

    /**
     * 잠재 -> 신규 보정 (벌크)
     * - 이력은 afterCommit(AUTO)에서 이미 남기는 구조면 여기선 segment만 보정해도 OK
     */
    @Transactional
    public int fixPotentialToNew() {
        int updated = segmentRebuildBatchRepository.bulkPromotePotentialToNew();
        log.info("[BATCH][SEGMENT] fixPotentialToNew updated={}", updated);
        return updated;
    }

    /**
     * 신규 -> 일반 승격 (벌크 + 이력 저장)
     * - triggerType = BATCH
     * - referenceId는 MVP에서는 null (필요하면 다음 단계에서 계약 id 조회 추가)
     */
    @Transactional
    public int fixNewToNormalWithHistory() {

        // 1) 이력 저장을 위해 대상 목록을 먼저 확보
        List<Long> targets = segmentRebuildBatchRepository.findNewToNormalTargetCustomerIds();
        if (targets.isEmpty()) {
            log.info("[BATCH][SEGMENT] fixNewToNormalWithHistory targets=0");
            return 0;
        }

        // 2) 벌크 UPDATE
        int updated = segmentRebuildBatchRepository.bulkPromoteNewToNormal();
        log.info("[BATCH][SEGMENT] fixNewToNormalWithHistory updated={} targets={}", updated, targets.size());

        // 3) 이력 저장 (BATCH)
        for (Long customerId : targets) {
            historyCommandRepository.save(
                    HistoryCommandEntity.builder()
                            .customerId(customerId)
                            .previousSegmentId(SEG_NEW)
                            .currentSegmentId(SEG_NORMAL)
                            .reason("첫 계약 후 3개월 경과")
                            .triggerType(SegmentChangeTriggerType.BATCH)
                            .referenceType(SegmentChangeReferenceType.CONTRACT)
                            .referenceId(null)
                            .build()
            );
        }

        return updated;
    }
}
