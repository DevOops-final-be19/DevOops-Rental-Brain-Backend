package com.devoops.rentalbrain.contract.command.service;

import com.devoops.rentalbrain.business.contract.command.repository.ContractCommandRepository;
import com.devoops.rentalbrain.business.contract.command.service.ContractStatusScheduler;
import com.devoops.rentalbrain.product.productlist.command.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContractStatusSchedulerTest {
    @Mock
    private ContractCommandRepository contractCommandRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ContractStatusScheduler contractStatusScheduler;

    @BeforeEach
    void setUp() {
        // 필요 시 공통 세팅
    }

    @Test
    @DisplayName("계약 상태 배치 정상 흐름 - 만료/임박/연체 처리 성공")
    void runContractStatusBatch_success() {
        // given
        List<Long> expiredContractIds = List.of(1L, 2L);
        List<Long> overdueTargets = List.of(2L);

        when(contractCommandRepository.updateToExpireImminent(any(), any()))
                .thenReturn(3);

        when(contractCommandRepository.findExpiredContractIds(any()))
                .thenReturn(expiredContractIds);

        when(contractCommandRepository.updateToClosedByIds(expiredContractIds))
                .thenReturn(2);

        when(contractCommandRepository.findContractsExpiredOneDayAgo(any()))
                .thenReturn(overdueTargets);

        when(itemRepository.updateItemsToOverdueExceptRepair(2L))
                .thenReturn(5);

        // when
        contractStatusScheduler.runContractStatusBatch();

        // then
        verify(contractCommandRepository, times(1))
                .updateToExpireImminent(any(), any());

        verify(contractCommandRepository, times(1))
                .findExpiredContractIds(any());

        verify(contractCommandRepository, times(1))
                .updateToClosedByIds(expiredContractIds);

        verify(contractCommandRepository, times(1))
                .findContractsExpiredOneDayAgo(any());

        verify(itemRepository, times(1))
                .updateItemsToOverdueExceptRepair(2L);
    }

    @Test
    @DisplayName("만료 계약이 없을 경우 CLOSED 업데이트는 호출되지 않는다")
    void runContractStatusBatch_noExpiredContracts() {
        // given
        when(contractCommandRepository.updateToExpireImminent(any(), any()))
                .thenReturn(0);

        when(contractCommandRepository.findExpiredContractIds(any()))
                .thenReturn(List.of());

        when(contractCommandRepository.findContractsExpiredOneDayAgo(any()))
                .thenReturn(List.of());

        // when
        contractStatusScheduler.runContractStatusBatch();

        // then
        verify(contractCommandRepository, never())
                .updateToClosedByIds(any());

        verify(itemRepository, never())
                .updateItemsToOverdueExceptRepair(anyLong());
    }

    @Test
    @DisplayName("만료 하루 지난 계약이 여러 개일 경우 아이템 상태 업데이트 반복 호출")
    void runContractStatusBatch_multipleOverdueTargets() {
        // given
        List<Long> overdueTargets = List.of(1L, 2L, 3L);

        when(contractCommandRepository.updateToExpireImminent(any(), any()))
                .thenReturn(1);

        when(contractCommandRepository.findExpiredContractIds(any()))
                .thenReturn(List.of());

        when(contractCommandRepository.findContractsExpiredOneDayAgo(any()))
                .thenReturn(overdueTargets);

        when(itemRepository.updateItemsToOverdueExceptRepair(anyLong()))
                .thenReturn(2);

        // when
        contractStatusScheduler.runContractStatusBatch();

        // then
        verify(itemRepository, times(3))
                .updateItemsToOverdueExceptRepair(anyLong());
    }
}
