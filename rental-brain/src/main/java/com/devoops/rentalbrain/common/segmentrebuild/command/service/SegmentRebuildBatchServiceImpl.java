package com.devoops.rentalbrain.common.segmentrebuild.command.service;

import com.devoops.rentalbrain.common.segmentrebuild.command.entity.RiskTransitionCommandEntity;
import com.devoops.rentalbrain.common.segmentrebuild.command.repository.RiskTransitionCommandRepository;
import com.devoops.rentalbrain.common.segmentrebuild.command.repository.SegmentRebuildBatchRepository;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.domain.SegmentChangeReferenceType;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.domain.SegmentChangeTriggerType;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.entity.HistoryCommandEntity;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.repository.HistoryCommandRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class SegmentRebuildBatchServiceImpl implements SegmentRebuildBatchService {

    private final SegmentRebuildBatchRepository segmentRebuildBatchRepository;

    // 일반 세그먼트 이력 (customer_segment_history)
    private final HistoryCommandRepository historyCommandRepository;

    // 리스크 이력 (customer_risk_transition_history)
    private final RiskTransitionCommandRepository riskTransitionCommandRepository;

    private static final long SEG_POTENTIAL = 1L;
    private static final long SEG_NEW       = 2L;
    private static final long SEG_NORMAL    = 3L;
    private static final long SEG_RISK      = 4L;
    private static final long SEG_VIP       = 5L;
    private static final long SEG_BLACKLIST = 6L;
    private static final long SEG_EXPANSION = 7L;

    @Autowired
    public SegmentRebuildBatchServiceImpl(SegmentRebuildBatchRepository segmentRebuildBatchRepository,
                                          HistoryCommandRepository historyCommandRepository,
                                          RiskTransitionCommandRepository riskTransitionCommandRepository) {
        this.segmentRebuildBatchRepository = segmentRebuildBatchRepository;
        this.historyCommandRepository = historyCommandRepository;
        this.riskTransitionCommandRepository = riskTransitionCommandRepository;
    }

    @Override
    @Transactional
    public int fixPotentialToNew() {
        int updated = segmentRebuildBatchRepository.bulkPromotePotentialToNew();
        log.info("[BATCH][세그먼트] 잠재→신규 보정 {}건", updated);
        return updated;
    }

    @Override
    @Transactional
    public int fixNewToNormalWithHistory() {
        List<Long> targets = segmentRebuildBatchRepository.findNewToNormalTargetCustomerIds();
        if (targets.isEmpty()) {
            log.info("[BATCH][세그먼트] 신규→일반 대상 0건");
            return 0;
        }

        int updated = segmentRebuildBatchRepository.bulkPromoteNewToNormal();
        log.info("[BATCH][세그먼트] 신규→일반 보정 {}건 (대상 {}명)", updated, targets.size());

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

    @Override
    @Transactional
    public int fixNormalToVipWithHistory() {
        List<Long> targets = segmentRebuildBatchRepository.findNormalToVipTargetCustomerIds();
        if (targets.isEmpty()) {
            log.info("[BATCH][세그먼트] 일반→VIP 대상 0건");
            return 0;
        }

        int updated = segmentRebuildBatchRepository.bulkPromoteNormalToVip();
        log.info("[BATCH][세그먼트] 일반→VIP 보정 {}건 (대상 {}명)", updated, targets.size());

        for (Long customerId : targets) {
            historyCommandRepository.save(
                    HistoryCommandEntity.builder()
                            .customerId(customerId)
                            .previousSegmentId(SEG_NORMAL)
                            .currentSegmentId(SEG_VIP)
                            .reason("누적 계약기간 36개월 이상 또는 총 계약금액 3억원 이상")
                            .triggerType(SegmentChangeTriggerType.BATCH)
                            .referenceType(SegmentChangeReferenceType.CONTRACT)
                            .referenceId(null)
                            .build()
            );
        }

        return updated;
    }

    @Override
    @Transactional
    public int fixToRiskWithHistory() {

        List<SegmentRebuildBatchRepository.RiskTargetRow> targets =
                segmentRebuildBatchRepository.findToRiskTargets();

        if (targets.isEmpty()) {
            log.info("[BATCH][RISK] 이탈위험 대상 0건");
            return 0;
        }

        int updated = segmentRebuildBatchRepository.bulkPromoteToRisk();

        for (var t : targets) {

            String reason;
            switch (t.getReasonCode()) {
                case "TERMINATION"      -> reason = "해지 요청 고객";
                case "EXPIRING_1_3M"    -> reason = "계약 만료 1~3개월 전";
                case "OVERDUE_LT_3M"    -> reason = "연체 3개월 미만 발생";
                case "LOW_SAT"          -> reason = "최근 피드백 평균 2.5점 이하";
                case "ENDED_WITHIN_3M"  -> reason = "계약 종료 후 3개월 이내";
                default                 -> reason = "이탈 위험 감지";
            }

            riskTransitionCommandRepository.save(
                    RiskTransitionCommandEntity.builder()
                            .customerId(t.getCustomerId())
                            .fromSegmentId(t.getFromSegmentId())
                            .toSegmentId(SEG_RISK)
                            .reasonCode(t.getReasonCode())
                            .reason(reason)
                            .triggerType("BATCH")
                            .referenceType("RULE")
                            .referenceId(null)
                            .changedAt(LocalDateTime.now())
                            .build()
            );
        }

        log.info("[BATCH][RISK] 이탈위험 보정 {}건 (대상 {}명)", updated, targets.size());
        return updated;
    }

    @Override
    @Transactional
    public int fixRiskToBlacklistWithHistory() {

        List<SegmentRebuildBatchRepository.BlacklistTargetRow> targets =
                segmentRebuildBatchRepository.findRiskToBlacklistTargets();

        if (targets.isEmpty()) {
            log.info("[BATCH][RISK] 블랙리스트 대상 0건");
            return 0;
        }

        int updated = segmentRebuildBatchRepository.bulkPromoteRiskToBlacklist();
        log.info("[BATCH][RISK] 블랙리스트 보정 {}건 (대상 {}명)", updated, targets.size());

        for (var t : targets) {
            riskTransitionCommandRepository.save(
                    RiskTransitionCommandEntity.builder()
                            .customerId(t.getCustomerId())
                            .fromSegmentId(SEG_RISK)
                            .toSegmentId(SEG_BLACKLIST)
                            .reasonCode("OVERDUE_90D")
                            .reason("연체 90일 이상 감지")
                            .triggerType("BATCH")
                            .referenceType(t.getReferenceType())
                            .referenceId(t.getReferenceId())
                            .changedAt(LocalDateTime.now())
                            .build()
            );
        }

        return updated;
    }


    @Override
    @Transactional
    public int fixToExpansionWithHistory() {

        List<SegmentRebuildBatchRepository.ExpansionTargetRow> targets =
                segmentRebuildBatchRepository.findToExpansionTargets();

        if (targets.isEmpty()) {
            log.info("[BATCH][세그먼트] 확장 의사 고객 대상 0건");
            return 0;
        }

        int updated = segmentRebuildBatchRepository.bulkPromoteToExpansion();
        log.info("[BATCH][세그먼트] 확장 의사 고객 보정 {}건 (대상 {}명)", updated, targets.size());

        for (var t : targets) {
            String reason;
            switch (t.getReasonCode()) {
                case "UPSALE_GROWTH_20P" -> reason = "최근 3개월 계약금액이 직전 3개월 대비 20% 이상 증가";
                case "RENEWAL_3_6M_HIGH_SAT" -> reason = "만료 3~6개월 전 + 최근 만족도 4.0점 이상";
                default -> reason = "확장 의사 신호 감지";
            }

            historyCommandRepository.save(
                    HistoryCommandEntity.builder()
                            .customerId(t.getCustomerId())
                            .previousSegmentId(t.getFromSegmentId())
                            .currentSegmentId(SEG_EXPANSION)
                            .reason(reason)
                            .triggerType(SegmentChangeTriggerType.BATCH)
                            .referenceType(SegmentChangeReferenceType.SYSTEM_RULE)
                            .referenceId(null)
                            .build()
            );
        }

        return updated;
    }


    @Override
    @Transactional
    public int fixRiskToNormalWithHistory() {

        List<SegmentRebuildBatchRepository.RiskToNormalTargetRow> targets =
                segmentRebuildBatchRepository.findRiskToNormalTargets();

        if (targets.isEmpty()) {
            log.info("[BATCH][RISK] 이탈위험→일반(복귀) 대상 0건");
            return 0;
        }

        int updated = segmentRebuildBatchRepository.bulkDemoteRiskToNormal();
        log.info("[BATCH][RISK] 이탈위험→일반(복귀) 보정 {}건 (대상 {}명)", updated, targets.size());

        // (A) 일반 세그먼트 이력 저장
        for (var t : targets) {
            historyCommandRepository.save(
                    HistoryCommandEntity.builder()
                            .customerId(t.getCustomerId())
                            .previousSegmentId(SEG_RISK)
                            .currentSegmentId(SEG_NORMAL)
                            .reason("이탈 위험 조건 해소(연체/해지요청 없음, 활성 계약 유지)")
                            .triggerType(SegmentChangeTriggerType.BATCH)
                            .referenceType(SegmentChangeReferenceType.SYSTEM_RULE) // enum 없으면 맞는 값으로 변경
                            .referenceId(null)
                            .build()
            );

//            // (B) 리스크 전이 이력도 남김(리스크 해제)
//            riskTransitionCommandRepository.save(
//                    RiskTransitionCommandEntity.builder()
//                            .customerId(t.getCustomerId())
//                            .fromSegmentId(SEG_RISK)
//                            .toSegmentId(SEG_NORMAL)
//                            .reasonCode(t.getReasonCode())  // 'RISK_CLEARED'
//                            .reason("이탈 위험 해제(복귀)")
//                            .triggerType("BATCH")
//                            .referenceType("RULE")
//                            .referenceId(null)
//                            .changedAt(LocalDateTime.now())
//                            .build()
//            );
        }

        return updated;
    }
}
