package com.devoops.rentalbrain.business.campaign.command.service;

import com.devoops.rentalbrain.business.campaign.command.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PromotionStatusScheduler {
    private final PromotionRepository promotionRepository;

    @Scheduled(cron = "0 0 6 * * *")
    @Transactional
    public void updatePromotionStatus() {
        promotionRepository.updatePromotionStatus();
    }
}
