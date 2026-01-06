package com.devoops.rentalbrain.contract.command.service;

import com.devoops.rentalbrain.business.contract.command.dto.PaymentDetailRequestDTO;
import com.devoops.rentalbrain.business.contract.command.entity.PaymentDetailCommandEntity;
import com.devoops.rentalbrain.business.contract.command.repository.PaymentDetailCommandRepository;
import com.devoops.rentalbrain.business.contract.command.service.PaymentDetailCommandServiceImpl;
import com.devoops.rentalbrain.product.productlist.command.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentDetailCommandServiceImplTest {

    @Mock
    private PaymentDetailCommandRepository paymentDetailCommandRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private PaymentDetailCommandServiceImpl paymentDetailCommandService;

    // ================================
    // completePayment
    // ================================

    @Test
    @DisplayName("결제 완료 처리 성공")
    void completePayment_success() {
        // given
        Long paymentDetailId = 1L;
        Long contractId = 10L;

        PaymentDetailCommandEntity entity = new PaymentDetailCommandEntity();
        entity.setId(paymentDetailId);
        entity.setContractId(contractId);
        entity.setPaymentStatus("P");

        LocalDateTime paidAt = LocalDateTime.of(2026, 1, 6, 10, 30);

        PaymentDetailRequestDTO dto = new PaymentDetailRequestDTO();
        dto.setPaymentActual(paidAt);

        when(paymentDetailCommandRepository.findById(paymentDetailId))
                .thenReturn(Optional.of(entity));
        when(itemRepository.addMonthlySalesByContract(contractId))
                .thenReturn(3);

        // when
        paymentDetailCommandService.completePayment(paymentDetailId, dto);

        // then
        assertEquals("C", entity.getPaymentStatus());
        assertEquals(paidAt, entity.getPaymentActual());
        assertEquals(0, entity.getOverdueDays());

        verify(itemRepository, times(1))
                .addMonthlySalesByContract(contractId);
    }

    @Test
    @DisplayName("결제 완료 실패 - 이미 완료된 결제")
    void completePayment_fail_alreadyCompleted() {
        // given
        Long paymentDetailId = 1L;

        PaymentDetailCommandEntity entity = new PaymentDetailCommandEntity();
        entity.setId(paymentDetailId);
        entity.setPaymentStatus("C");

        when(paymentDetailCommandRepository.findById(paymentDetailId))
                .thenReturn(Optional.of(entity));

        LocalDateTime paidAt = LocalDateTime.of(2026, 1, 6, 10, 30);

        PaymentDetailRequestDTO dto = new PaymentDetailRequestDTO();
        dto.setPaymentActual(paidAt);

        // when & then
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> paymentDetailCommandService.completePayment(paymentDetailId, dto)
        );

        assertEquals("이미 완료된 결제입니다.", ex.getMessage());

        verify(itemRepository, never())
                .addMonthlySalesByContract(any());
    }

    @Test
    @DisplayName("결제 완료 실패 - 결제 내역 없음")
    void completePayment_fail_notFound() {
        // given
        Long paymentDetailId = 99L;

        when(paymentDetailCommandRepository.findById(paymentDetailId))
                .thenReturn(Optional.empty());

        PaymentDetailRequestDTO dto = new PaymentDetailRequestDTO();

        // when & then
        assertThrows(
                EntityNotFoundException.class,
                () -> paymentDetailCommandService.completePayment(paymentDetailId, dto)
        );
    }

    // ================================
    // autoMarkAsNonPayment
    // ================================

    @Test
    @DisplayName("미납 자동 전환 처리")
    void autoMarkAsNonPayment_success() {
        // given
        when(paymentDetailCommandRepository.markAsNonPayment(any(LocalDateTime.class)))
                .thenReturn(5);

        // when
        paymentDetailCommandService.autoMarkAsNonPayment();

        // then
        verify(paymentDetailCommandRepository, times(1))
                .markAsNonPayment(any(LocalDateTime.class));
    }

    // ================================
    // increaseOverdueDays
    // ================================

    @Test
    @DisplayName("미납 연체 일수 증가 처리")
    void increaseOverdueDays_success() {
        // given
        when(paymentDetailCommandRepository.increaseOverdueDaysForNonPayment())
                .thenReturn(7);

        // when
        paymentDetailCommandService.increaseOverdueDays();

        // then
        verify(paymentDetailCommandRepository, times(1))
                .increaseOverdueDaysForNonPayment();
    }
}
