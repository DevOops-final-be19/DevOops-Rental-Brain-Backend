package com.devoops.rentalbrain.customer.overdue.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemOverdueAutoCreateScheduler {

    private final ItemOverdueAutoCreateService service;

    @Scheduled(cron = "0 0 8 * * ?") // 매일 오전 8시
    public void run() {
        service.createItemOverdueFromExpiredContract();
    }
}