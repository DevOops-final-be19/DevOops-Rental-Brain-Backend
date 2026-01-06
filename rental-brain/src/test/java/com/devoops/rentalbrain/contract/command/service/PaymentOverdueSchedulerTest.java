package com.devoops.rentalbrain.contract.command.service;

import com.devoops.rentalbrain.business.contract.command.service.PaymentDetailCommandService;
import com.devoops.rentalbrain.business.contract.command.service.PaymentOverdueScheduler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentOverdueSchedulerTest {

    @Mock
    private PaymentDetailCommandService paymentDetailCommandService;

    @InjectMocks
    private PaymentOverdueScheduler paymentOverdueScheduler;

    @Test
    @DisplayName("연체 스케줄러 실행 시 미납 처리 및 연체 일수 증가 호출")
    void runDailyOverdueIncrease_success() {
        // when
        paymentOverdueScheduler.runDailyOverdueIncrease();

        // then
        verify(paymentDetailCommandService, times(1))
                .autoMarkAsNonPayment();

        verify(paymentDetailCommandService, times(1))
                .increaseOverdueDays();

        verifyNoMoreInteractions(paymentDetailCommandService);
    }
}
