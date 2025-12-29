package com.devoops.rentalbrain.business.campaign.command.repository;

import com.devoops.rentalbrain.business.campaign.command.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    Promotion findByPromotionCode(String promotionCode);

    void deleteByPromotionCode(String promotionCode);

    @Modifying
    @Query("""
    UPDATE Promotion p
        SET p.status =
            CASE
                WHEN p.startDate IS NOT NULL AND p.endDate IS NOT NULL
                     AND CURRENT_TIMESTAMP > p.startDate AND CURRENT_TIMESTAMP < p.endDate
                    THEN 'A'
                WHEN p.startDate IS NOT NULL
                     AND CURRENT_TIMESTAMP < p.startDate
                    THEN 'P'
                WHEN p.endDate IS NOT NULL
                     AND CURRENT_TIMESTAMP > p.endDate
                    THEN 'C'
            END
            WHERE p.type = 'M'
""")
    void updatePromotionStatus();
}
