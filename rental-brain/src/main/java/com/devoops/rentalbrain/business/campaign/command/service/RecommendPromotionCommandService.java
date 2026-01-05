package com.devoops.rentalbrain.business.campaign.command.service;

import com.devoops.rentalbrain.business.campaign.command.dto.InsertRecommendPromotionDTO;

public interface RecommendPromotionCommandService {
    void insertRecommendPromotion(InsertRecommendPromotionDTO recommendPromotionDTO);

    void deleteRecommendPromotion(Long recommendPromotionId);

    void updateRecommendPromotion(Long recommendPromotionId);
}
