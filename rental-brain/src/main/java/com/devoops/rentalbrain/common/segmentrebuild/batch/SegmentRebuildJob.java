package com.devoops.rentalbrain.common.segmentrebuild.batch;

import com.devoops.rentalbrain.common.segmentrebuild.command.service.SegmentRebuildBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SegmentRebuildJob implements Job {

    private final SegmentRebuildBatchService segmentRebuildBatchService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        log.info("[세그먼트 배치 시작] 고객 세그먼트 자동 보정 작업을 시작합니다.");

        try {
            // ✅ touched + 우선순위 적용된 단일 실행
            Map<String, Integer> result =
                    segmentRebuildBatchService.runWithPriorityOnce();

            log.info("[세그먼트 배치 완료] {}", result);

        } catch (Exception e) {
            log.error("[세그먼트 배치 실패] 세그먼트 자동 보정 중 오류 발생", e);
            throw new JobExecutionException(e);
        }
    }
}
