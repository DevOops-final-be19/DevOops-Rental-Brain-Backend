package com.devoops.rentalbrain.customer.overdue.query.controller;

import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.customer.overdue.query.dto.ItemOverdueDetailDTO;
import com.devoops.rentalbrain.customer.overdue.query.dto.ItemOverdueListDTO;
import com.devoops.rentalbrain.customer.overdue.query.dto.PayOverdueDetailDTO;
import com.devoops.rentalbrain.customer.overdue.query.dto.PayOverdueListDTO;
import com.devoops.rentalbrain.customer.overdue.query.service.OverdueQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/customers/overdues")
@RequiredArgsConstructor
@Tag(name = "연체 조회 (Query)", description = "수납 연체 / 제품 연체 조회 API (목록, 상세)")
public class OverdueQueryController {

    private final OverdueQueryService overdueQueryService;

    @Operation(
            summary = "수납 연체 목록 조회",
            description = """
                    수납 연체 목록을 조회합니다.
                   
                    - 페이징, 검색 조건(type, keyword) 지원
                    - 상태(P: 미해결, C: 해결) 필터 가능
                    """)
    @ApiResponses({@ApiResponse(responseCode = "200", description = "수납 연체 목록 조회 성공")})
    @GetMapping("/pay")
    public PageResponseDTO<PayOverdueListDTO> getPayOverdueList(Criteria criteria,
                                                                @RequestParam(required = false) String status) {
        return overdueQueryService.getPayOverdueList(criteria, status);
    }

    @Operation(
            summary = "수납 연체 상세 조회",
            description = """
                    특정 수납 연체의 상세 정보를 조회합니다.
                    
                    - 계약 정보
                    - 납부 예정일
                    - 연체 기간
                    - 해결 여부
                    """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수납 연체 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "수납 연체 정보 없음")
    })
    @GetMapping("/pay/{overdueId}")
    public PayOverdueDetailDTO getPayOverdueDetail(@PathVariable Long overdueId) {
        return overdueQueryService.getPayOverdueDetail(overdueId);
    }

    @Operation(
            summary = "제품 연체 목록 조회",
            description = """
                    제품 연체 목록을 조회합니다.
                    
                    - 계약 단위 제품 연체
                    - 연체 수량 기준
                    - 페이징 / 검색 지원
                    """
    )
    @ApiResponses({@ApiResponse(responseCode = "200", description = "제품 연체 목록 조회 성공")})
    @GetMapping("/item")
    public PageResponseDTO<ItemOverdueListDTO> getItemOverdueList(Criteria criteria,
                                                                  @RequestParam(required = false) String status) {
        return overdueQueryService.getItemOverdueList(criteria, status);
    }

    @Operation(
            summary = "제품 연체 상세 조회",
            description = """
                    제품 연체 상세 정보를 조회합니다.
                    
                    - 계약 정보
                    - 연체 수량
                    - 연체 상태
                    - 연체된 제품 목록
                    """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "제품 연체 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "제품 연체 정보 없음")})
    @GetMapping("/item/{overdueId}")
    public Map<String, Object> getItemOverdueDetail(@PathVariable Long overdueId) {
        return overdueQueryService.getItemOverdueDetail(overdueId);
    }
}
