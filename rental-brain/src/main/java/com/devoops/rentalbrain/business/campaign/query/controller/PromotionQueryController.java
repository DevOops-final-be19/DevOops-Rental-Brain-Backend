package com.devoops.rentalbrain.business.campaign.query.controller;

import com.devoops.rentalbrain.business.campaign.query.service.PromotionQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/campaign")
@Slf4j
public class PromotionQueryController {
    private final PromotionQueryService promotionQueryService;

    @Autowired
    public PromotionQueryController(PromotionQueryService promotionQueryService) {
        this.promotionQueryService = promotionQueryService;
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
