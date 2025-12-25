package com.devoops.rentalbrain.business.campaign.command.repository;

import com.devoops.rentalbrain.business.campaign.command.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Modifying
    @Query("""
    UPDATE Coupon c
        SET c.status =
            CASE
                WHEN c.startDate IS NULL AND c.endDate IS NULL
                    THEN 'A'
                WHEN c.startDate IS NOT NULL
                     AND CURRENT_TIMESTAMP < c.startDate
                    THEN 'P'
                WHEN c.endDate IS NOT NULL
                     AND CURRENT_TIMESTAMP > c.endDate
                    THEN 'C'
                ELSE 'A'
            END
""")
    int updateCouponStatus();

    Coupon findByCouponCode(String couponCode);

    void deleteByCouponCode(String couponCode);
}
