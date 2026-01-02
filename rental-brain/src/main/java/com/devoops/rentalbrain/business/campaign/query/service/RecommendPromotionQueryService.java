package com.devoops.rentalbrain.business.campaign.query.service;

import com.devoops.rentalbrain.business.campaign.query.dto.RecommendPromotionDTO;

import java.util.List;

public interface RecommendPromotionQueryService {
    List<RecommendPromotionDTO> recommendPromotionsBySurveyId(Long surveyId);

    RecommendPromotionDTO recommendPromotionById(Long recommendPromotionId);

    RecommendPromotionDTO recommendPromotionOne();
}
