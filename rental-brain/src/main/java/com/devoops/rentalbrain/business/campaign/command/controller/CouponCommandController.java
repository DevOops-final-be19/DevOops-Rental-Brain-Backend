package com.devoops.rentalbrain.business.campaign.command.controller;

import com.devoops.rentalbrain.business.campaign.command.service.CouponCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupon")
@Slf4j
public class CouponCommandController {
    private final CouponCommandService couponCommandService;

    @Autowired
    public CouponCommandController(CouponCommandService couponCommandService) {
        this.couponCommandService = couponCommandService;
    }
}
