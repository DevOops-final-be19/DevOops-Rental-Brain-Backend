package com.devoops.rentalbrain.business.campaign.command.controller;

import com.devoops.rentalbrain.business.campaign.command.dto.InsertPromotionDTO;
import com.devoops.rentalbrain.business.campaign.command.dto.ModifyPromotionDTO;
import com.devoops.rentalbrain.business.campaign.command.service.PromotionCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/promotion")
@Slf4j
@Tag(name = "프로모션 관리(Command)",
        description = "프로모션 등록, 수정, 삭제 관련 API")
public class PromotionCommandController {
    private final PromotionCommandService promotionCommandService;

    @Autowired
    public PromotionCommandController(PromotionCommandService promotionCommandService) {
        this.promotionCommandService = promotionCommandService;
    }

    @Operation(
            summary = "프로모션 등록",
            description = "프로모션을 등록합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @PostMapping("/insert")
    public String insertPromotion(@RequestBody InsertPromotionDTO promotionDTO) {
        String result = promotionCommandService.insertPromotion(promotionDTO);
        return result;
    }

    @Operation(
            summary = "프로모션 정보 수정",
            description = "프로모션의 정보를 수정합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @PutMapping("/update/{proCode}")
    public String updatePromotion(@PathVariable String proCode, @RequestBody ModifyPromotionDTO promotionDTO) {
        String result = promotionCommandService.updatePromotion(proCode, promotionDTO);
        return result;
    }

    @Operation(
            summary = "프로모션 삭제",
            description = "프로모션을 삭제합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @DeleteMapping("/delete/{proCode}")
    public String deletePromotion(@PathVariable String proCode) {
        String result = promotionCommandService.deletePromotion(proCode);
        return result;
    }


    @Operation(
            summary = "프로모션 사용 이력",
            description = "계약 시 선택된 프로모션으로 사용 이력을 남깁니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @PostMapping("/log/{promotionId}/{contractId}")
    public String createPromotionLog(@PathVariable("promotionId") Long promotionId,
                               @PathVariable("contractId") Long contractId) {
        String result = promotionCommandService.createPromotionLog(promotionId, contractId);
        return result;
    }
}
