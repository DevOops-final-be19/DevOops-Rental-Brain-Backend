package com.devoops.rentalbrain.business.campaign.command.service;

import com.devoops.rentalbrain.business.campaign.command.dto.InsertRecommendPromotionDTO;
import com.devoops.rentalbrain.business.campaign.command.entity.RecommendPromotion;
import com.devoops.rentalbrain.business.campaign.command.repository.RecommendPromotionRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecommendPromotionCommandServiceImpl implements RecommendPromotionCommandService {
    private final RecommendPromotionRepository recommendPromotionRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RecommendPromotionCommandServiceImpl(RecommendPromotionRepository recommendPromotionRepository,
                                                ModelMapper modelMapper) {
        this.recommendPromotionRepository = recommendPromotionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public void insertRecommendPromotion(InsertRecommendPromotionDTO recommendPromotionDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        RecommendPromotion recommendPromotion = modelMapper.map(recommendPromotionDTO, RecommendPromotion.class);
        recommendPromotion.setIsUsed("N");
        recommendPromotionRepository.save(recommendPromotion);
    }

    @Override
    @Transactional
    public void deleteRecommendPromotion(Long recommendPromotionId) {
        recommendPromotionRepository.deleteById(recommendPromotionId);
    }

    @Override
    @Transactional
    public void updateRecommendPromotion(Long recommendPromotionId) {
        RecommendPromotion recommendPromotion =
                recommendPromotionRepository.findById(recommendPromotionId).orElse(null);
        if (recommendPromotion != null) {
            recommendPromotion.setIsUsed("Y");
            recommendPromotionRepository.save(recommendPromotion);
        }
    }
}
