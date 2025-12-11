package com.devoops.rentalbrain.business.campaign.command.service;

import com.devoops.rentalbrain.business.campaign.command.repository.PromotionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromotionCommandServiceImpl implements PromotionCommandService {
    private final ModelMapper modelMapper;
    private final PromotionRepository promotionRepository;

    @Autowired
    public PromotionCommandServiceImpl(ModelMapper modelMapper, PromotionRepository promotionRepository) {
        this.modelMapper = modelMapper;
        this.promotionRepository = promotionRepository;
    }

}
