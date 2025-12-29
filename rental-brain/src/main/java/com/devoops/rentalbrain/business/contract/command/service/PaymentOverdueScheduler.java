package com.devoops.rentalbrain.business.contract.command.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentOverdueScheduler {

    private final PaymentDetailCommandService  paymentDetailCommandService;

    public PaymentOverdueScheduler(PaymentDetailCommandService paymentDetailCommandService) {
        this.paymentDetailCommandService = paymentDetailCommandService;
    }



    @Scheduled(cron = "0 0 6 * * *")
    public void runDailyOverdueIncrease() {
        log.info("[Scheduler] 자동 미납 처리 시작");
        paymentDetailCommandService.autoMarkAsNonPayment();

        log.info("[Scheduler] 미납 연체일수 증가 시작");
        paymentDetailCommandService.increaseOverdueDays();

        log.info("[Scheduler] 연체 처리 스케줄 종료");
    }

}
