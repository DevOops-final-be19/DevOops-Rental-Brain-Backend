package com.devoops.rentalbrain.product.maintenance.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AfterServiceScheduler {

    private final AfterServiceCommandService service;

    // 매 30분마다 완료 + 생성을 하나의 흐름으로 처리
    @Scheduled(cron = "0 */30 * * * *")
    public void autoProcessAfterService() {
        service.autoCompleteAndCreateNext();
    }
}
