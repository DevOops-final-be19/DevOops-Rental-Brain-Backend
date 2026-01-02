package com.devoops.rentalbrain.business.contract.command.service;

import com.devoops.rentalbrain.business.contract.command.repository.ContractCommandRepository;
import com.devoops.rentalbrain.product.productlist.command.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class ContractStatusScheduler {
    private final ContractCommandRepository contractCommandRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public ContractStatusScheduler(
            ContractCommandRepository contractCommandRepository,
            ItemRepository itemRepository
    ) {
        this.contractCommandRepository = contractCommandRepository;
        this.itemRepository = itemRepository;
    }

    /**
     * 계약 상태 자동 변경
     *
     * 흐름:
     * P → I → C
     * 계약 만료 시 하루 지난 후 해당 아이템 S → O (연체중)
     */
    @Scheduled(cron = "0 0 6 * * *")
    @Transactional
    public void runContractStatusBatch() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMonthLater = now.plusMonths(1);

        log.info("[BATCH][CONTRACT_STATUS] START now={}", now);

        // 1. 만료 임박 처리

        int imminentUpdated = contractCommandRepository.updateToExpireImminent(
                now, oneMonthLater
        );

        log.info(
                "[BATCH][STEP1][EXPIRE_IMMINENT] updatedCount={}",
                imminentUpdated
        );

        // 2. 만료 대상 계약 조회
        List<Long> expiredContractIds =
                contractCommandRepository.findExpiredContractIds(now);

        log.info(
                "[BATCH][STEP2][EXPIRED_FOUND] count={}, ids={}",
                expiredContractIds.size(),
                expiredContractIds
        );

        // 3. 계약 상태 → C
        if (!expiredContractIds.isEmpty()) {
            int closedUpdated =
            contractCommandRepository.updateToClosedByIds(
                    expiredContractIds
            );

            log.info(
                    "[BATCH][STEP3][CLOSED] updatedCount={}",
                    closedUpdated
            );

        }

        // 4. 만료 후 하루 지난 계약의 아이템만 점검중(O)으로
        List<Long> inspectionTargets =
                contractCommandRepository.findContractsExpiredOneDayAgo(now);

        log.info(
                "[BATCH][STEP4][OVERDUE_TARGET] count={}, ids={}",
                inspectionTargets.size(),
                inspectionTargets
        );

        // 5. 아이템 상태 변경
        for (Long contractId : inspectionTargets) {
            int updated = itemRepository.updateItemsToOverdueExceptRepair(contractId);
            log.info(
                    "[BATCH][ITEM_OVERDUE] contractId={}, updatedItems={}",
                    contractId,
                    updated
            );
        }

        log.info(
                "[BATCH][CONTRACT_STATUS] expired={}, inspectionTargets={}",
                expiredContractIds.size(),
                inspectionTargets.size()
        );
    }
}
