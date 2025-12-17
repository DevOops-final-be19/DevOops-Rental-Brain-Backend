package com.devoops.rentalbrain.business.campaign.command.service;

import com.devoops.rentalbrain.business.campaign.command.dto.InsertCouponDTO;
import com.devoops.rentalbrain.business.campaign.command.dto.ModifyCouponDTO;

public interface CouponCommandService {
    String insertCoupon(InsertCouponDTO couponDTO);

    String updateCoupon(Long couponId, ModifyCouponDTO couponDTO);

    String deleteCoupon(Long couponId);

    String createIssuedCoupon(Long couponId, Long contractId);
}
