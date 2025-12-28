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

        log.info("세그먼트 배치 시작: 고객 세그먼트 자동 보정 작업을 시작합니다.");

        try {
            int potentialToNew = segmentRebuildBatchService.fixPotentialToNew();
            int newToNormal    = segmentRebuildBatchService.fixNewToNormalWithHistory();
            int normalToVip    = segmentRebuildBatchService.fixNormalToVipWithHistory();
            int toRiskUpdated = segmentRebuildBatchService.fixToRiskWithHistory();


            log.info(
                    "[세그먼트 배치 완료] 잠재→신규: {}건, 신규→일반: {}건, 일반→VIP: {}건, 이탈위험: {}건",
                    potentialToNew,
                    newToNormal,
                    normalToVip,
                    toRiskUpdated
            );

        } catch (Exception e) {
            log.error("[세그먼트 배치 실패] 세그먼트 자동 보정 중 오류 발생", e);
            throw new JobExecutionException(e);
        }
    }
}
