package com.devoops.rentalbrain.customer.customersupport.query.controller;

import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.FeedbackDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.FeedbackSearchDTO;
import com.devoops.rentalbrain.customer.customersupport.query.service.CustomersupportQueryFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "고객 문의 - 피드백 (Query)", description = "피드백 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/feedbacks")
public class CustomersupportQueryFeedbackController {

    private final CustomersupportQueryFeedbackService feedbackService;

    @Operation(summary = "피드백 목록 조회", description = "검색 조건에 따른 피드백 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/all")
    public ResponseEntity<PageResponseDTO<FeedbackDTO>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer category,
            @RequestParam(required = false) Integer star,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int amount) {

        FeedbackSearchDTO criteria = new FeedbackSearchDTO(page, amount);
        criteria.setKeyword(keyword);
        criteria.setStatus(status);
        criteria.setCategory(category);
        criteria.setStar(star);
        criteria.setSortBy(sortBy);
        criteria.setSortOrder(sortOrder);

        return ResponseEntity.ok(feedbackService.getFeedbackList(criteria));
    }

    @Operation(summary = "피드백 상세 조회", description = "특정 ID의 피드백 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 피드백"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FeedbackDTO> detail(@PathVariable Long id) {
        return ResponseEntity.ok(feedbackService.getFeedbackDetail(id));
    }

    @Operation(summary = "피드백 KPI 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/kpi")
    public ResponseEntity<Map<String, Object>> getKpi() {
        return ResponseEntity.ok(feedbackService.getFeedbackKpi());
    }
}