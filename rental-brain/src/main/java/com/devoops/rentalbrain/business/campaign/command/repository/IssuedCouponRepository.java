package com.devoops.rentalbrain.business.campaign.command.repository;

import com.devoops.rentalbrain.business.campaign.command.entity.IssuedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssuedCouponRepository extends JpaRepository<IssuedCoupon, Long> {
}
