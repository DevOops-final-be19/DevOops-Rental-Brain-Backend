package com.devoops.rentalbrain.business.campaign.command.service;

import com.devoops.rentalbrain.business.campaign.command.dto.InsertRecommendCouponDTO;
import com.devoops.rentalbrain.business.campaign.command.entity.RecommendCoupon;
import com.devoops.rentalbrain.business.campaign.command.repository.RecommendCouponRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecommendCouponCommandServiceImpl implements RecommendCouponCommandService {
    private final RecommendCouponRepository recommendCouponRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RecommendCouponCommandServiceImpl(RecommendCouponRepository recommendCouponRepository,
                                             ModelMapper modelMapper) {
        this.recommendCouponRepository = recommendCouponRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public void insertRecommendCoupon(InsertRecommendCouponDTO recommendCouponDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        RecommendCoupon recommendCoupon = modelMapper.map(recommendCouponDTO, RecommendCoupon.class);
        recommendCouponRepository.save(recommendCoupon);
    }

    @Override
    @Transactional
    public void DeleteRecommendCoupon(Long recommendCouponId) {
        recommendCouponRepository.deleteById(recommendCouponId);
    }
}
