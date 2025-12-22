package com.devoops.rentalbrain.common.segmentrebuild.quartz;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class QuartzConfig {

    public static final String SEGMENT_REBUILD_JOB = "segmentRebuildJob";

    @Bean
    public JobDetail segmentRebuildJobDetail() {
        return JobBuilder.newJob(SegmentRebuildJob.class)
                .withIdentity(SEGMENT_REBUILD_JOB)
                .storeDurably()
                .build();
    }

    /*
     * 매일 새벽 2시 (Asia/Seoul)
     * Cron: 초 분 시 일 월 요일
     */
    @Bean
    public Trigger segmentRebuildDailyTrigger(JobDetail segmentRebuildJobDetail) {
        TimeZone kst = TimeZone.getTimeZone("Asia/Seoul");

        return TriggerBuilder.newTrigger()
                .forJob(segmentRebuildJobDetail)
                .withIdentity("segmentRebuildDailyTrigger")
                .withSchedule(
                        CronScheduleBuilder.cronSchedule("0 0 2 * * ?")               // 새벽 2시에
//                        CronScheduleBuilder.cronSchedule("0 0/1 * * * ?")   // 1분마다
                                .inTimeZone(kst)
                )
                .build();
    }
}