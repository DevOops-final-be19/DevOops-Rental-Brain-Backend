package com.devoops.rentalbrain.business.campaign.command.controller;

import com.devoops.rentalbrain.business.campaign.command.dto.InsertRecommendPromotionDTO;
import com.devoops.rentalbrain.business.campaign.command.service.RecommendPromotionCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recommend/promotion/")
@Tag(name = "추천 프로모션 관리(Command)",
        description = "추천 프로모션 생성, 삭제 관련 API")
public class RecommendPromotionCommandController {
    private final RecommendPromotionCommandService recommendPromotionCommandService;

    @Autowired
    public RecommendPromotionCommandController(RecommendPromotionCommandService recommendPromotionCommandService) {
        this.recommendPromotionCommandService = recommendPromotionCommandService;
    }

    @Operation(
            summary = "추천 프로모션 등록",
            description = "추천 프로모션이 생성됩니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @PostMapping("insert")
    public void insertRecommendPromotion(@RequestBody InsertRecommendPromotionDTO recommendPromotionDTO) {
        recommendPromotionCommandService.insertRecommendPromotion(recommendPromotionDTO);
    }

    @Operation(
            summary = "추천 프로모션 수정",
            description = "추천 프로모션이 실제로 등록되면 생성여부를 수정합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @PutMapping("update/{recommendPromotionId}")
    public void updateRecommendPromotion(@PathVariable Long recommendPromotionId) {
        recommendPromotionCommandService.updateRecommendPromotion(recommendPromotionId);
    }

    @Operation(
            summary = "추천 프로모션 삭제",
            description = "추천 프로모션을 삭제합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @DeleteMapping("delete/{recommendPromotionId}")
    public void deleteRecommendPromotion(@PathVariable Long recommendPromotionId) {
        recommendPromotionCommandService.deleteRecommendPromotion(recommendPromotionId);
    }
}
