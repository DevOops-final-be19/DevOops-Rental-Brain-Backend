package com.devoops.rentalbrain.product.maintenance.query.controller;

import com.devoops.rentalbrain.product.maintenance.query.dto.RentalItemForAsDTO;
import com.devoops.rentalbrain.product.maintenance.query.service.AfterServiceItemQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/as")
@Tag(name = "고객 별 렌탈 중인 AS/정기점검 가능 제품 조회")
public class AfterServiceItemQueryController {

    private final AfterServiceItemQueryService afterServiceItemQueryService;

    @Operation(
            summary = "기업별 렌탈 중인 제품 조회 (AS 일정 추가용)",
            description = "선택한 기업(customerId)의 계약 중(P/I)인 렌탈 자산만 조회한다."
    )
    @GetMapping("/customers/{customerId}/rental-items")
    public List<RentalItemForAsDTO> getRentalItemsByCustomer(
            @PathVariable Long customerId
    ) {
        return afterServiceItemQueryService.getRentalItemsByCustomer(customerId);
    }
}
