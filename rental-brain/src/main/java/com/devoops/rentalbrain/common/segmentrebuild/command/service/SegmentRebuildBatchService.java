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
     * - 이력은 "이미 afterCommit에서 남기는 구조"를 쓰고 있으니
     *   배치에서는 일단 segment만 보정 (필요하면 나중에 BATCH 이력도 추가 가능)
     */
    @Transactional
    public int fixPotentialToNew() {
        int updated = segmentRebuildBatchRepository.bulkPromotePotentialToNew();
        log.info("[BATCH][SEGMENT] fixPotentialToNew updated={}", updated);
        return updated;
    }

    /**
     * 신규 -> 일반 승격 (벌크 + 이력 저장)
     * 규칙:
     * - 신규(2)
     * - (해지 제외) 첫 계약 start_date 3개월 경과
     * - 활성 계약 존재( start_date <= today <= start_date + period )
     */
    @Transactional
    public int fixNewToNormalWithHistory() {
        // 1) 대상 customerId 목록 먼저 확보 (누가 바뀌는지 알아야 이력 남김)
        List<Long> targets = segmentRebuildBatchRepository.findNewToNormalTargetCustomerIds();
        if (targets.isEmpty()) {
            log.info("[BATCH][SEGMENT] fixNewToNormalWithHistory targets=0");
            return 0;
        }

        // 2) 실제 벌크 업데이트
        int updated = segmentRebuildBatchRepository.bulkPromoteNewToNormal();
        log.info("[BATCH][SEGMENT] fixNewToNormalWithHistory updated={} targets={}", updated, targets.size());

        // 3) 이력 저장 (trigger=BATCH)
        for (Long customerId : targets) {
            historyCommandRepository.save(
                    HistoryCommandEntity.builder()
                            .customerId(customerId)
                            .previousSegmentId(SEG_NEW)
                            .currentSegmentId(SEG_NORMAL)
                            .reason("첫 계약 후 3개월 경과")
                            .triggerType(SegmentChangeTriggerType.BATCH)
                            .referenceType(SegmentChangeReferenceType.CONTRACT)
                            .referenceId(null) // MVP: 계약 id까지 꼭 필요하면 다음 단계에서 추가 조회
                            .build()
            );
        }

        return updated;
    }
}
