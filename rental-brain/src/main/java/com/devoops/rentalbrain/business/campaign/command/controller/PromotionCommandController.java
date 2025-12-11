package com.devoops.rentalbrain.business.campaign.command.controller;

import com.devoops.rentalbrain.business.campaign.command.service.PromotionCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/promotion")
@Slf4j
public class PromotionCommandController {
    private final PromotionCommandService promotionCommandService;

    @Autowired
    public PromotionCommandController(PromotionCommandService promotionCommandService) {
        this.promotionCommandService = promotionCommandService;
    }
}
