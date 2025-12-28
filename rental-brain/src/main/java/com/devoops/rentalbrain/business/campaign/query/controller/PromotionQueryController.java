package com.devoops.rentalbrain.business.campaign.query.controller;

import com.devoops.rentalbrain.business.campaign.query.dto.PromotionDTO;
import com.devoops.rentalbrain.business.campaign.query.dto.PromotionWithContractDTO;
import com.devoops.rentalbrain.business.campaign.query.service.PromotionQueryService;
import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promotion")
@Slf4j
@Tag(name = "프로모션 관리(Query)",
        description = "프로모션 정보 조회 관련 API")
public class PromotionQueryController {
    private final PromotionQueryService promotionQueryService;

    @Autowired
    public PromotionQueryController(PromotionQueryService promotionQueryService) {
        this.promotionQueryService = promotionQueryService;
    }

    @Operation(
            summary = "프로모션 목록 조회",
            description = "전체 프로모션 목록을 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("/read-list")
    public ResponseEntity<PageResponseDTO<PromotionDTO>> readPromotionList(@RequestParam(defaultValue = "1") int page,
                                                                           @RequestParam(defaultValue = "10") int size) {
        Criteria criteria = new Criteria(page, size);
        PageResponseDTO<PromotionDTO> promotionList = promotionQueryService.readPromotionList(criteria);
        return ResponseEntity.ok().body(promotionList);
    }

    @Operation(
            summary = "프로모션 검색",
            description = "코드나 이름으로 프로모션을 검색합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("/search/{keyword}")
    public ResponseEntity<PageResponseDTO<PromotionDTO>> searchPromotion(@PathVariable String keyword,
                                                                   @RequestParam(defaultValue = "1") int page,
                                                                   @RequestParam(defaultValue = "10") int size) {
        Criteria criteria = new Criteria(page, size);
        PageResponseDTO<PromotionDTO> promotionList = promotionQueryService.searchPromotion(keyword, criteria);
        return ResponseEntity.ok().body(promotionList);
    }

    @Operation(
            summary = "프로모션 목록 유형 필터링",
            description = "전체 프로모션 목록을 유형으로 필터링합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("/filter-type/{type}")
    public ResponseEntity<PageResponseDTO<PromotionDTO>> filteringPromotionByType(@PathVariable String type,
                                                                            @RequestParam(defaultValue = "1") int page,
                                                                            @RequestParam(defaultValue = "10") int size) {
        Criteria criteria = new Criteria(page, size);
        PageResponseDTO<PromotionDTO> promotionList = promotionQueryService.filterPromotionByType(type, criteria);
        return ResponseEntity.ok().body(promotionList);
    }

    @Operation(
            summary = "프로모션 목록 상태 필터링",
            description = "전체 프로모션 목록을 상태로 필터링합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("/filter-status/{status}")
    public ResponseEntity<PageResponseDTO<PromotionDTO>> filteringPromotionByStatus(@PathVariable String status,
                                                                              @RequestParam(defaultValue = "1") int page,
                                                                              @RequestParam(defaultValue = "10") int size) {
        Criteria criteria = new Criteria(page, size);
        PageResponseDTO<PromotionDTO> promotionList = promotionQueryService.filterPromotionByStatus(status, criteria);
        return ResponseEntity.ok().body(promotionList);
    }

    @Operation(
            summary = "계약 시 프로모션 조회",
            description = "계약 시 세그먼트에 따라 프로모션을 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("/use-contract/{segment}")
    public ResponseEntity<List<PromotionWithContractDTO>> useContract(@PathVariable Integer segment) {
        List<PromotionWithContractDTO> promotionList = promotionQueryService.useContractPromotion(segment);
        return ResponseEntity.ok().body(promotionList);
    }

    @Operation(
            summary = "프로모션 상세 조회",
            description = "각 프로모션별 정보를 상세 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @GetMapping("/read-detail/{proCode}")
    public ResponseEntity<PromotionDTO> readDetail(@PathVariable String proCode) {
        PromotionDTO promotion = promotionQueryService.readDetailPromotion(proCode);
        return ResponseEntity.ok().body(promotion);
    }
}
