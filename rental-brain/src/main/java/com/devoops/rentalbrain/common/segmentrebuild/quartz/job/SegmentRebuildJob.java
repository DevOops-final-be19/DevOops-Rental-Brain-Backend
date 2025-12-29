<<<<<<<< HEAD:rental-brain/src/main/java/com/devoops/rentalbrain/common/segmentrebuild/quartz/job/SegmentRebuildJob.java
package com.devoops.rentalbrain.common.segmentrebuild.quartz.job;
========
package com.devoops.rentalbrain.common.segmentrebuild.batch;
>>>>>>>> 5294e0d (세그먼트 상태변경 쿼츠 진행중 #69):rental-brain/src/main/java/com/devoops/rentalbrain/common/segmentrebuild/batch/SegmentRebuildJob.java

import com.devoops.rentalbrain.common.segmentrebuild.quartz.service.SegmentBatchCommandService;
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

    private final SegmentBatchCommandService segmentBatchCommandService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            int a = segmentBatchCommandService.fixPotentialToNew();
            int b = segmentBatchCommandService.promoteNewToNormal();

            log.info("[QUARTZ][SEGMENT] potential->new updated={}", a);
            log.info("[QUARTZ][SEGMENT] new->normal updated={}", b);

        } catch (Exception e) {
            log.error("쿼츠 업데이트 job failed", e);
            throw new JobExecutionException(e);
        }
    }
}
