package com.devoops.rentalbrain.business.campaign.query.mapper;

import com.devoops.rentalbrain.business.campaign.query.dto.RecommendCouponDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecommendCouponMapper {
    List<RecommendCouponDTO> selectRecommendCouponsBySurveyId(Long surveyId);

    RecommendCouponDTO selectRecommendCouponById(Long recommendCouponId);

    RecommendCouponDTO selectRecommendCouponOne();
}
