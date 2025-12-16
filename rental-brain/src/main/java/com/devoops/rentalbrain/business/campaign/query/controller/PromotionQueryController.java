package com.devoops.rentalbrain.business.campaign.query.controller;

import com.devoops.rentalbrain.business.campaign.query.service.PromotionQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/campaign")
@Slf4j
@Tag(name = "프로모션 관리(Query)",
        description = "프로모션 정보 조회 관련 API")
public class PromotionQueryController {
    private final PromotionQueryService promotionQueryService;

    @Autowired
    public PromotionQueryController(PromotionQueryService promotionQueryService) {
        this.promotionQueryService = promotionQueryService;
    }

}
