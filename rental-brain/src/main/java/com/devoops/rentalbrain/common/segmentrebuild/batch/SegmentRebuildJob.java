package com.devoops.rentalbrain.common.segmentrebuild.batch;

import com.devoops.rentalbrain.common.segmentrebuild.command.service.SegmentRebuildBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SegmentRebuildJob implements Job {

    private final SegmentRebuildBatchService segmentRebuildBatchService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        log.info("[세그먼트 배치 시작] 고객 세그먼트 자동 보정 작업을 시작합니다.");

        try {
            int potentialToNew   = segmentRebuildBatchService.fixPotentialToNew();
            int newToNormal      = segmentRebuildBatchService.fixNewToNormalWithHistory();
            int normalToVip      = segmentRebuildBatchService.fixNormalToVipWithHistory();

            // 위험/블랙이 확장보다 우선순위 UP
            int toRiskUpdated    = segmentRebuildBatchService.fixToRiskWithHistory();
            int riskToBlacklist  = segmentRebuildBatchService.fixRiskToBlacklistWithHistory();

            // 확장 의사 고객
            int toExpansion      = segmentRebuildBatchService.fixToExpansionWithHistory();

            // 일반 복귀
            int riskToNormal = segmentRebuildBatchService.fixRiskToNormalWithHistory();

            log.info(
                    "[세그먼트 배치 완료] 잠재→신규: {}건, 신규→일반: {}건, 일반→VIP: {}건, 이탈위험: {}건, 블랙리스트: {}건, 확장 의사: {}건, 일반 복귀: {}건",
                    potentialToNew,
                    newToNormal,
                    normalToVip,
                    toRiskUpdated,
                    riskToBlacklist,
                    toExpansion,
                    riskToNormal
            );

        } catch (Exception e) {
            log.error("[세그먼트 배치 실패] 세그먼트 자동 보정 중 오류 발생", e);
            throw new JobExecutionException(e);
        }
    }
}
