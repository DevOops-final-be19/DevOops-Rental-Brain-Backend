package com.devoops.rentalbrain.customer.customersupport.command.controller;

import com.devoops.rentalbrain.customer.customersupport.command.service.CustomersupportCommandFeedbackService;
import com.devoops.rentalbrain.customer.customersupport.query.dto.FeedbackDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "고객 문의 - 피드백 (Command)", description = "피드백 등록, 수정, 삭제 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/feedbacks")
public class CustomersupportCommandFeedbackController {

    private final CustomersupportCommandFeedbackService feedbackService;

    @Operation(summary = "신규 피드백 등록", description = "새로운 피드백을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/insertFeedback")
    public ResponseEntity<Void> createFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        feedbackService.createFeedback(feedbackDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "피드백 수정", description = "기존 피드백 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 피드백 ID"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PutMapping("/updateFeedback/{id}")
    public ResponseEntity<Void> updateFeedback(@PathVariable Long id, @RequestBody FeedbackDTO feedbackDTO) {
        feedbackService.updateFeedback(id, feedbackDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "피드백 삭제", description = "피드백 내역을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 피드백 ID"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @DeleteMapping("/deleteFeedback/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.ok().build();
    }
}