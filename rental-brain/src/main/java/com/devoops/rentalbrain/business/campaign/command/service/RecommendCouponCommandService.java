package com.devoops.rentalbrain.business.campaign.command.service;

import com.devoops.rentalbrain.business.campaign.command.dto.InsertRecommendCouponDTO;

public interface RecommendCouponCommandService {
    void insertRecommendCoupon(InsertRecommendCouponDTO recommendCouponDTO);

    void deleteRecommendCoupon(Long recommendCouponId);

    void updateRecommendCoupon(Long recommendCouponId);
}
