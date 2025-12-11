package com.devoops.rentalbrain.business.campaign.command.service;

import com.devoops.rentalbrain.business.campaign.command.repository.CouponRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponCommandServiceImpl implements CouponCommandService {
    private final ModelMapper modelMapper;
    private final CouponRepository couponRepository;

    @Autowired
    public CouponCommandServiceImpl(ModelMapper modelMapper, CouponRepository couponRepository) {
        this.modelMapper = modelMapper;
        this.couponRepository = couponRepository;
    }
}
