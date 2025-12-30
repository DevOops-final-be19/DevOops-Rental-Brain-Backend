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
import java.util.*;

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

    /*
     * ✅ 권장: 우선순위 + touched 공유로 "한 배치 실행 당 고객 1회 이동" 보장.
     * 우선순위:
     * 1) 이탈위험 → 블랙리스트
     * 2) 고객 → 이탈위험
     * 3) 잠재 → 신규
     * 4) 신규 → 일반
     * 5) 일반 → VIP
     * 6) 고객 → 확장 의사
     * 7) 이탈위험 → 일반(복귀) (맨 마지막, 계약하면 복귀)
     */

    @Override
    @Transactional
    public Map<String, Integer> runWithPriorityOnce() {
        Set<Long> touched = new HashSet<>();
        Map<String, Integer> out = new LinkedHashMap<>();

        out.put("riskToBlacklistUpdated", fixRiskToBlacklistWithHistory(touched));
        out.put("toRiskUpdated",          fixToRiskWithHistory(touched));
        out.put("potentialToNewUpdated",  fixPotentialToNewWithHistory(touched));
        out.put("newToNormalUpdated",     fixNewToNormalWithHistory(touched));
        out.put("normalToVipUpdated",     fixNormalToVipWithHistory(touched));
        out.put("toExpansionUpdated",     fixToExpansionWithHistory(touched));
        out.put("riskToNormalUpdated",    fixRiskToNormalWithHistory(touched)); // 맨 마지막

        return out;
    }

    @Override
    @Transactional
    public int fixPotentialToNew() {
        return fixPotentialToNewWithHistory(new HashSet<>());
    }

    @Override
    @Transactional
    public int fixNewToNormalWithHistory() {
        return fixNewToNormalWithHistory(new HashSet<>());
    }

    @Override
    @Transactional
    public int fixNormalToVipWithHistory() {
        return fixNormalToVipWithHistory(new HashSet<>());
    }

    @Override
    @Transactional
    public int fixToRiskWithHistory() {
        return fixToRiskWithHistory(new HashSet<>());
    }

    @Override
    @Transactional
    public int fixRiskToBlacklistWithHistory() {
        return fixRiskToBlacklistWithHistory(new HashSet<>());
    }

    @Override
    @Transactional
    public int fixToExpansionWithHistory() {
        return fixToExpansionWithHistory(new HashSet<>());
    }

    @Override
    @Transactional
    public int fixRiskToNormalWithHistory() {
        return fixRiskToNormalWithHistory(new HashSet<>());
    }

    // touched 적용 내부 구현부
    private int fixRiskToBlacklistWithHistory(Set<Long> touched) {

        List<SegmentRebuildBatchRepository.BlacklistTargetRow> targets =
                segmentRebuildBatchRepository.findRiskToBlacklistTargetsExcluding(excludeList(touched));

        if (targets.isEmpty()) {
            log.info("[BATCH][RISK] 이탈위험→블랙리스트 대상 0건");
            return 0;
        }

        List<Long> ids = targets.stream().map(SegmentRebuildBatchRepository.BlacklistTargetRow::getCustomerId).toList();
        int updated = segmentRebuildBatchRepository.bulkPromoteRiskToBlacklistByIds(ids);

        log.info("[BATCH][RISK] 이탈위험→블랙리스트 보정 {}건 (대상 {}명)", updated, ids.size());

        for (var t : targets) {
            riskTransitionCommandRepository.save(
                    RiskTransitionCommandEntity.builder()
                            .customerId(t.getCustomerId())
                            .fromSegmentId(SEG_RISK)
                            .toSegmentId(SEG_BLACKLIST)
                            .reasonCode("OVERDUE_90D")
                            .reason("연체 90일 이상 감지")
                            .triggerType(SegmentChangeTriggerType.BATCH.name())
                            .referenceType(t.getReferenceType())
                            .referenceId(t.getReferenceId())
                            .changedAt(LocalDateTime.now())
                            .build()
            );
        }

        touched.addAll(ids);
        return updated;
    }

    private int fixToRiskWithHistory(Set<Long> touched) {

        List<SegmentRebuildBatchRepository.RiskTargetRow> targets =
                segmentRebuildBatchRepository.findToRiskTargetsExcluding(excludeList(touched));

        if (targets.isEmpty()) {
            log.info("[BATCH][RISK] 고객→이탈위험 대상 0건");
            return 0;
        }

        List<Long> ids = targets.stream().map(SegmentRebuildBatchRepository.RiskTargetRow::getCustomerId).toList();
        int updated = segmentRebuildBatchRepository.bulkPromoteToRiskByIds(ids);

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
                            .triggerType(SegmentChangeTriggerType.BATCH.name())
                            .referenceType(SegmentChangeReferenceType.SYSTEM_RULE.name())
                            .referenceId(null)
                            .changedAt(LocalDateTime.now())
                            .build()
            );
        }

        log.info("[BATCH][RISK] 고객→이탈위험 보정 {}건 (대상 {}명)", updated, ids.size());

        touched.addAll(ids);
        return updated;
    }

    /**
     * ✅ 잠재→신규 + 이력까지 저장
     */
    private int fixPotentialToNewWithHistory(Set<Long> touched) {

        List<Long> targets =
                segmentRebuildBatchRepository.findPotentialToNewTargetCustomerIdsExcluding(excludeList(touched));

        if (targets.isEmpty()) {
            log.info("[BATCH][세그먼트] 잠재→신규 대상 0건");
            return 0;
        }

        int updated = segmentRebuildBatchRepository.bulkPromotePotentialToNewByIds(targets);
        log.info("[BATCH][세그먼트] 잠재→신규 보정 {}건 (대상 {}명)", updated, targets.size());

        for (Long customerId : targets) {
            historyCommandRepository.save(
                    HistoryCommandEntity.builder()
                            .customerId(customerId)
                            .previousSegmentId(SEG_POTENTIAL)
                            .currentSegmentId(SEG_NEW)
                            .reason("첫 계약 등록")
                            .triggerType(SegmentChangeTriggerType.BATCH)
                            .referenceType(SegmentChangeReferenceType.CONTRACT)
                            .referenceId(null)
                            .build()
            );
        }

        touched.addAll(targets);
        return updated;
    }

    private int fixNewToNormalWithHistory(Set<Long> touched) {

        List<Long> targets =
                segmentRebuildBatchRepository.findNewToNormalTargetCustomerIdsExcluding(excludeList(touched));

        if (targets.isEmpty()) {
            log.info("[BATCH][세그먼트] 신규→일반 대상 0건");
            return 0;
        }

        int updated = segmentRebuildBatchRepository.bulkPromoteNewToNormalByIds(targets);
        log.info("[BATCH][세그먼트] 신규→일반 보정 {}건 (대상 {}명)", updated, targets.size());

        for (Long customerId : targets) {
            historyCommandRepository.save(
                    HistoryCommandEntity.builder()
                            .customerId(customerId)
                            .previousSegmentId(SEG_NEW)
                            .currentSegmentId(SEG_NORMAL)
                            .reason("첫 계약 후 3개월 경과")
                            .triggerType(SegmentChangeTriggerType.BATCH)
                            .referenceType(SegmentChangeReferenceType.SYSTEM_RULE)
                            .referenceId(null)
                            .build()
            );
        }

        touched.addAll(targets);
        return updated;
    }

    private int fixNormalToVipWithHistory(Set<Long> touched) {

        List<Long> targets =
                segmentRebuildBatchRepository.findNormalToVipTargetCustomerIdsExcluding(excludeList(touched));

        if (targets.isEmpty()) {
            log.info("[BATCH][세그먼트] 일반→VIP 대상 0건");
            return 0;
        }

        int updated = segmentRebuildBatchRepository.bulkPromoteNormalToVipByIds(targets);
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

        touched.addAll(targets);
        return updated;
    }

    private int fixToExpansionWithHistory(Set<Long> touched) {

        List<SegmentRebuildBatchRepository.ExpansionTargetRow> targets =
                segmentRebuildBatchRepository.findToExpansionTargetsExcluding(excludeList(touched));

        if (targets.isEmpty()) {
            log.info("[BATCH][세그먼트] 확장 의사 고객 대상 0건");
            return 0;
        }

        List<Long> ids = targets.stream().map(SegmentRebuildBatchRepository.ExpansionTargetRow::getCustomerId).toList();
        int updated = segmentRebuildBatchRepository.bulkPromoteToExpansionByIds(ids);

        log.info("[BATCH][세그먼트] 확장 의사 고객 보정 {}건 (대상 {}명)", updated, ids.size());

        for (var t : targets) {
            String reason;
            switch (t.getReasonCode()) {
                case "UPSALE_GROWTH_20P"       -> reason = "최근 3개월 계약금액이 직전 3개월 대비 20% 이상 증가";
                case "RENEWAL_3_6M_HIGH_SAT"   -> reason = "만료 3~6개월 전 + 최근 만족도 4.0점 이상";
                default                        -> reason = "확장 의사 신호 감지";
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

        touched.addAll(ids);
        return updated;
    }

    /**
     * ✅ 복귀는 "계약하면 일반으로" + 맨 마지막 실행
     * (리스크 전이 테이블에는 제약조건 때문에 저장하지 않음)
     */
    private int fixRiskToNormalWithHistory(Set<Long> touched) {

        List<SegmentRebuildBatchRepository.RiskToNormalTargetRow> targets =
                segmentRebuildBatchRepository.findRiskToNormalTargetsExcluding(excludeList(touched));

        if (targets.isEmpty()) {
            log.info("[BATCH][RISK] 이탈위험→일반(복귀) 대상 0건");
            return 0;
        }

        List<Long> ids = targets.stream().map(SegmentRebuildBatchRepository.RiskToNormalTargetRow::getCustomerId).toList();
        int updated = segmentRebuildBatchRepository.bulkDemoteRiskToNormalByIds(ids);

        log.info("[BATCH][RISK] 이탈위험→일반(복귀) 보정 {}건 (대상 {}명)", updated, ids.size());

        for (var t : targets) {
            historyCommandRepository.save(
                    HistoryCommandEntity.builder()
                            .customerId(t.getCustomerId())
                            .previousSegmentId(SEG_RISK)
                            .currentSegmentId(SEG_NORMAL)
                            .reason("계약 체결로 이탈위험 해제(복귀)")
                            .triggerType(SegmentChangeTriggerType.BATCH)
                            .referenceType(SegmentChangeReferenceType.CONTRACT)
                            .referenceId(null)
                            .build()
            );
        }

        touched.addAll(ids);
        return updated;
    }

    /**
     * nativeQuery NOT IN (:excludeIds)에서 빈 리스트가 들어가면 깨질 수 있어서 더미를 넣음.
     */
    private List<Long> excludeList(Set<Long> touched) {
        if (touched == null || touched.isEmpty()) return List.of(-1L);
        return new ArrayList<>(touched);
    }
}
