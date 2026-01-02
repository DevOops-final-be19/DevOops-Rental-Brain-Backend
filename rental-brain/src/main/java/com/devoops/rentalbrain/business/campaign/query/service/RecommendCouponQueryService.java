package com.devoops.rentalbrain.business.campaign.query.service;

import com.devoops.rentalbrain.business.campaign.query.dto.RecommendCouponDTO;

import java.util.List;

public interface RecommendCouponQueryService {
    List<RecommendCouponDTO> recommendCouponsBySurveyId(Long surveyId);

    RecommendCouponDTO recommendCouponById(Long recommendCouponId);

    RecommendCouponDTO recommendCouponOne();
}
