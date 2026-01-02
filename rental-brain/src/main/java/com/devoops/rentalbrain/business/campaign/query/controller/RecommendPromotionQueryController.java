package com.devoops.rentalbrain.business.campaign.query.controller;

import com.devoops.rentalbrain.business.campaign.query.dto.RecommendPromotionDTO;
import com.devoops.rentalbrain.business.campaign.query.service.RecommendPromotionQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recommend/promotion/")
@Tag(name = "추천 프로모션 관리(Query)",
        description = "추천 프로모션 정보 조회 관련 API")
public class RecommendPromotionQueryController {
    private final RecommendPromotionQueryService recommendPromotionQueryService;

    @Autowired
    public RecommendPromotionQueryController(RecommendPromotionQueryService recommendPromotionQueryService) {
        this.recommendPromotionQueryService = recommendPromotionQueryService;
    }

    @Operation(
            summary = "추천 프로모션 설문ID로 조회",
            description = "설문ID가 일치하는 추천 프로모션을 전체 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("all-read/{surveyId}")
    public ResponseEntity<List<RecommendPromotionDTO>> recommendPromotionsBySurveyId(@PathVariable Long surveyId) {
        List<RecommendPromotionDTO> recommendPromotions =
                recommendPromotionQueryService.recommendPromotionsBySurveyId(surveyId);
        return ResponseEntity.ok().body(recommendPromotions);
    }

    @Operation(
            summary = "추천 프로모션 개별 조회",
            description = "추천 프로모션을 개별 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("read-one/{recommendPromotionId}")
    public ResponseEntity<RecommendPromotionDTO> recommendPromotionById(@PathVariable Long recommendPromotionId) {
        RecommendPromotionDTO recommendPromotion =
                recommendPromotionQueryService.recommendPromotionById(recommendPromotionId);
        return ResponseEntity.ok().body(recommendPromotion);
    }

    @Operation(
            summary = "대시보드 추천 프로모션 조회",
            description = "대시보드에서 추천 프로모션을 최신순으로 한개만 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("read-one")
    public ResponseEntity<RecommendPromotionDTO> recommendPromotionOne() {
        RecommendPromotionDTO recommendPromotion = recommendPromotionQueryService.recommendPromotionOne();
        return ResponseEntity.ok().body(recommendPromotion);
    }
}

