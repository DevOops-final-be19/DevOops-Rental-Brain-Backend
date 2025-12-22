package com.devoops.rentalbrain.product.productlist.command.controller;

import com.devoops.rentalbrain.product.productlist.command.dto.ItemDTO;
import com.devoops.rentalbrain.product.productlist.command.dto.ModifyEachItemDTO;
import com.devoops.rentalbrain.product.productlist.command.dto.ModifyItemDTO;
import com.devoops.rentalbrain.product.productlist.command.service.ItemCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
@Tag(name = "제품 관리(Command)",
        description = "제품 등록, 수정, 삭제 관련 API")
public class ItemCommandController {
    private final ItemCommandService itemCommandService;

    @Autowired
    public ItemCommandController(ItemCommandService itemCommandService) {
        this.itemCommandService = itemCommandService;
    }

    @Operation(
            summary = "제품 등록",
            description = "제품을 등록합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @PostMapping("/insert")
    public void insertNewItem(@RequestBody ItemDTO itemDTO) {
        itemCommandService.insertNewItem(itemDTO);
    }

    @Operation(
            summary = "개별 제품 정보 수정",
            description = "개별 제품의 매출, 수리비, 상태, 최근 점검일을 수정합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @PutMapping("/update/{itemId}")
    public String updateItem(@PathVariable int itemId, @RequestBody ModifyEachItemDTO itemDTO) {
        String result = itemCommandService.updateItem(itemId, itemDTO);

        return result;
    }

    @Operation(
            summary = "제품 정보 수정",
            description = "제품의 제품명, 월 렌탈료, 카테고리를 수정합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @PutMapping("/update-name/{itemName}")
    public String updateItemName(@PathVariable String itemName, @RequestBody ModifyItemDTO itemDTO) {
        String result = itemCommandService.updateItemName(itemName, itemDTO);
        return result;
    }

    @Operation(
            summary = "제품 삭제",
            description = "제품을 삭제합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @DeleteMapping("/delete/{itemId}")
    public String deleteItem(@PathVariable int itemId) {
        String result = itemCommandService.deleteItem(itemId);

        return result;
    }
}
