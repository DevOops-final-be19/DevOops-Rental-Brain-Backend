package com.devoops.rentalbrain.customer.customersupport.query.controller;

import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.*;
import com.devoops.rentalbrain.customer.customersupport.query.service.CustomersupportQueryFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "피드백 조회(Query)",
        description = "피드백 목록 조회, 상세 조회 등 피드백 관련 조회(Query) API"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/feedbacks")
public class CustomersupportQueryFeedbackController {

    private final CustomersupportQueryFeedbackService queryService;

    @Operation(summary = "피드백 목록 조회", description = "제목 검색과 페이징을 적용하여 피드백 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/all")
    public ResponseEntity<PageResponseDTO<FeedbackDTO>> list(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        FeedbackSearchDTO criteria = new FeedbackSearchDTO(page, size);
        criteria.setTitle(title);

        return ResponseEntity.ok(queryService.getFeedbackList(criteria));
    }

    @Operation(summary = "피드백 상세 조회", description = "특정 피드백 ID에 대한 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 피드백"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FeedbackDTO> detail(@PathVariable Long id) {
        return ResponseEntity.ok(queryService.getFeedbackDetail(id));
    }
}