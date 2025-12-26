package com.devoops.rentalbrain.customer.overdue.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PayOverdueAutoCreateScheduler {

    private final PayOverdueAutoCreateService service;

    @Scheduled(cron = "0 0 8 * * ?") // 매일 아침 8시
    public void runOverdueAutoCreate() {
        service.createOverdueFromPaymentDetails();
    }
}
