package com.devoops.rentalbrain.business.campaign.query.service;

import com.devoops.rentalbrain.business.campaign.query.mapper.PromotionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromotionQueryServiceImpl implements PromotionQueryService {
    private final PromotionMapper campaignMapper;

    @Autowired
    public PromotionQueryServiceImpl(PromotionMapper campaignMapper) {
        this.campaignMapper = campaignMapper;
    }


}
