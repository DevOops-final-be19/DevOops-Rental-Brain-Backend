package com.devoops.rentalbrain.business.campaign.query.service;

import com.devoops.rentalbrain.business.campaign.query.dto.RecommendPromotionDTO;
import com.devoops.rentalbrain.business.campaign.query.mapper.RecommendPromotionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendPromotionQueryServiceImpl implements RecommendPromotionQueryService {
    private final RecommendPromotionMapper recommendPromotionMapper;

    @Autowired
    public RecommendPromotionQueryServiceImpl(RecommendPromotionMapper recommendPromotionMapper) {
        this.recommendPromotionMapper = recommendPromotionMapper;
    }

    @Override
    public List<RecommendPromotionDTO> recommendPromotionsBySurveyId(Long surveyId) {
        List<RecommendPromotionDTO> recommendpromotions =
                recommendPromotionMapper.selectRecommendPromotionsBySurveyId(surveyId);
        return recommendpromotions;
    }

    @Override
    public RecommendPromotionDTO recommendPromotionById(Long recommendPromotionId) {
        RecommendPromotionDTO recommendpromotion =
                recommendPromotionMapper.selectRecommendPromotionById(recommendPromotionId);
        return recommendpromotion;
    }

    @Override
    public RecommendPromotionDTO recommendPromotionOne() {
        RecommendPromotionDTO recommendpromotion =
                recommendPromotionMapper.selectRecommendPromotionOne();
        return recommendpromotion;
    }
}
