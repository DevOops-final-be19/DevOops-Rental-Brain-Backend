package com.devoops.rentalbrain.business.campaign.command.repository;

import com.devoops.rentalbrain.business.campaign.command.entity.RecommendPromotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendPromotionRepository extends JpaRepository<RecommendPromotion, Long> {
}
