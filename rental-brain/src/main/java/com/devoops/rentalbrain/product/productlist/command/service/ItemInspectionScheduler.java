package com.devoops.rentalbrain.product.productlist.command.service;

import com.devoops.rentalbrain.product.productlist.command.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class ItemInspectionScheduler {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemInspectionScheduler(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Scheduled(cron = "0 10 6 * * *")
    @Transactional
    public void restoreItemsAfterInspection() {

        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);

        int restoredCount =
                itemRepository.restoreInspectedItems(yesterday);

        log.info(
                "[ITEM][INSPECTION_COMPLETE] restoredCount={}",
                restoredCount
        );
    }
}
