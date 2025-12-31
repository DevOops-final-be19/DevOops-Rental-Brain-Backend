package com.devoops.rentalbrain.business.campaign.command.service;

import com.devoops.rentalbrain.business.campaign.command.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponStatusScheduler {
    private final CouponRepository couponRepository;

    @Scheduled(cron = "0 0 6 * * *")
    @Transactional
    public void updateCouponStatus() {
        couponRepository.updateCouponStatus();
    }
}
