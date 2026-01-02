package com.devoops.rentalbrain.business.campaign.query.mapper;

import com.devoops.rentalbrain.business.campaign.query.dto.RecommendPromotionDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecommendPromotionMapper {
    List<RecommendPromotionDTO> selectRecommendPromotionsBySurveyId(Long surveyId);

    RecommendPromotionDTO selectRecommendPromotionById(Long recommendPromotionId);

    RecommendPromotionDTO selectRecommendPromotionOne();
}
