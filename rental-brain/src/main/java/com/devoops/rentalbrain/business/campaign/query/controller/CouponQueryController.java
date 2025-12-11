package com.devoops.rentalbrain.business.campaign.query.controller;

import com.devoops.rentalbrain.business.campaign.query.service.CouponQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupon")
@Slf4j
public class CouponQueryController {
    private final CouponQueryService couponQueryService;

    @Autowired
    public CouponQueryController(CouponQueryService couponQueryService) {
        this.couponQueryService = couponQueryService;
    }
}
