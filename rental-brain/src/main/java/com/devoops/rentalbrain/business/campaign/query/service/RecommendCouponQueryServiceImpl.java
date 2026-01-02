package com.devoops.rentalbrain.business.campaign.query.service;

import com.devoops.rentalbrain.business.campaign.command.entity.RecommendCoupon;
import com.devoops.rentalbrain.business.campaign.query.dto.RecommendCouponDTO;
import com.devoops.rentalbrain.business.campaign.query.mapper.RecommendCouponMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendCouponQueryServiceImpl implements RecommendCouponQueryService {
    private final RecommendCouponMapper recommendCouponMapper;

    @Autowired
    public RecommendCouponQueryServiceImpl(RecommendCouponMapper recommendCouponMapper) {
        this.recommendCouponMapper = recommendCouponMapper;
    }

    @Override
    public List<RecommendCouponDTO> recommendCouponsBySurveyId(Long surveyId) {
        List<RecommendCouponDTO> recommendCoupons =
                recommendCouponMapper.selectRecommendCouponsBySurveyId(surveyId);
        return recommendCoupons;
    }

    @Override
    public RecommendCouponDTO recommendCouponById(Long recommendCouponId) {
        RecommendCouponDTO recommendCoupon = recommendCouponMapper.selectRecommendCouponById(recommendCouponId);
        return recommendCoupon;
    }

    @Override
    public RecommendCouponDTO recommendCouponOne() {
        RecommendCouponDTO recommendCoupon = recommendCouponMapper.selectRecommendCouponOne();
        return recommendCoupon;
    }
}
