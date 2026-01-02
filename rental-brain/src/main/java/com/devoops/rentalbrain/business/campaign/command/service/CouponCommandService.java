package com.devoops.rentalbrain.business.campaign.command.service;

import com.devoops.rentalbrain.business.campaign.command.dto.InsertCouponDTO;
import com.devoops.rentalbrain.business.campaign.command.dto.ModifyCouponDTO;

public interface CouponCommandService {
    String insertCoupon(InsertCouponDTO couponDTO);

    String updateCoupon(String couCode, ModifyCouponDTO couponDTO);

    String deleteCoupon(String couCode);

    String createIssuedCoupon(Long couponId, Long contractId);

    void updateIssuedCoupon(Long contractId);
}
