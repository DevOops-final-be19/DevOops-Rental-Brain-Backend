package com.devoops.rentalbrain.customer.customeranalysis.churnsnapshot.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerChurnSnapshotScheduler {

    private final CustomerChurnSnapshotJobService jobService;

    // 매월 1일 00:10에 전월 스냅샷 생성
    @Scheduled(cron = "0 0 6 1 * *", zone = "Asia/Seoul")
    public void runPrevMonth() {
        log.info("[ChurnSnapshotScheduler] start");
        jobService.runPrevMonthSnapshot();
        log.info("[ChurnSnapshotScheduler] done");
    }
}