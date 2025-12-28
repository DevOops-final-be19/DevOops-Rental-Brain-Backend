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
        try {
            int u1 = segmentRebuildBatchService.fixPotentialToNew();
            int u2 = segmentRebuildBatchService.fixNewToNormalWithHistory();

            log.info("[QUARTZ][SEGMENT] done potential->new={}, new->normal={}", u1, u2);
        } catch (Exception e) {
            log.error("[QUARTZ][SEGMENT] job failed", e);
            throw new JobExecutionException(e);
        }
    }
}
