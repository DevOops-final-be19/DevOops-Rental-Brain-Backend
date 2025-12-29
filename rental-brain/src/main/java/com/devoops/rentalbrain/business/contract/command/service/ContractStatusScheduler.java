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
     * 계약 만료 시 아이템 S → O (점검중)
     */
    @Scheduled(cron = "0 0 6 * * *")
    @Transactional
    public void runContractStatusBatch() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMonthLater = now.plusMonths(1);

        // 1. 만료 임박
        contractCommandRepository.updateToExpireImminent(
                now, oneMonthLater
        );

        // 2. 만료 대상 계약 조회
        List<Long> expiredContractIds =
                contractCommandRepository.findExpiredContractIds(now);

        if (!expiredContractIds.isEmpty()) {

            // 3. 계약 상태 → C
            contractCommandRepository.updateToClosedByIds(
                    expiredContractIds
            );

            // 4. 아이템 → 점검중(O)
            for (Long contractId : expiredContractIds) {
                itemRepository.updateItemsToInspection(contractId);
            }
        }

        log.info(
                "[BATCH][CONTRACT_STATUS] expiredCount={}",
                expiredContractIds.size()
        );
    }
}
