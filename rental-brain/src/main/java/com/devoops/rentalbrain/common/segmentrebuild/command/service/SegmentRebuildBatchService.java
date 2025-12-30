package com.devoops.rentalbrain.common.segmentrebuild.command.service;

import java.util.Map;

public interface SegmentRebuildBatchService {

    /**
     * ✅ 우선순위 + touched 공유로 "한 배치 실행당 고객 1회 이동" 보장.
     * 우선순위:
     * 1) 이탈위험 → 블랙리스트
     * 2) 고객 → 이탈위험
     * 3) 잠재 → 신규
     * 4) 신규 → 일반
     * 5) 일반 → VIP
     * 6) 고객 → 확장 의사
     * 7) 이탈위험 → 일반(복귀) (맨 마지막, 계약하면 복귀)
     *
     * @return 단계별 업데이트 건수 Map
     */
    Map<String, Integer> runWithPriorityOnce();

    int fixPotentialToNew();

    int fixNewToNormalWithHistory();
    int fixNormalToVipWithHistory();

    int fixToRiskWithHistory();
    int fixRiskToBlacklistWithHistory();

    int fixToExpansionWithHistory();

    int fixRiskToNormalWithHistory();
}
