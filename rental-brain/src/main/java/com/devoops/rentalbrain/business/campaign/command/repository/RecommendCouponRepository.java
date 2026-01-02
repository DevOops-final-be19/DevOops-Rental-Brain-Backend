package com.devoops.rentalbrain.business.campaign.command.repository;

import com.devoops.rentalbrain.business.campaign.command.entity.RecommendCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendCouponRepository extends JpaRepository<RecommendCoupon, Long> {
}
