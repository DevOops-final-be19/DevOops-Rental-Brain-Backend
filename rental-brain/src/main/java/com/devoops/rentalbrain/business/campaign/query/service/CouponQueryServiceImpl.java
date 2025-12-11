package com.devoops.rentalbrain.business.campaign.query.service;

import com.devoops.rentalbrain.business.campaign.query.mapper.CouponMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CouponQueryServiceImpl implements CouponQueryService {
    private final CouponMapper campaignMapper;

    @Autowired
    public CouponQueryServiceImpl(CouponMapper campaignMapper) {
        this.campaignMapper = campaignMapper;
    }
}
