package com.devoops.rentalbrain.customer.customersupport.command.controller;

import com.devoops.rentalbrain.customer.customersupport.command.dto.FeedbackDTO;
import com.devoops.rentalbrain.customer.customersupport.command.service.CustomersupportCommandFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "피드백 관리(Command)",
        description = "피드백 등록, 수정, 삭제 등 피드백 관련 명령(Command) API"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/feedbacks")
@Slf4j
public class CustomersupportCommandFeedbackController {

    private final CustomersupportCommandFeedbackService commandService;

    @Operation(summary = "피드백 등록", description = "신규 피드백을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/insertFeedback")
    public ResponseEntity<String> register(@RequestBody FeedbackDTO dto) {
        Long savedId = commandService.registerFeedback(dto);
        log.info("피드백 등록 완료 ID: {}", savedId);
        return ResponseEntity.ok("피드백이 정상적으로 등록되었습니다. (ID: " + savedId + ")");
    }

    @Operation(summary = "피드백 수정", description = "기존 피드백 내역을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 피드백 ID"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PutMapping("/updateFeedback/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody FeedbackDTO dto) {
        commandService.updateFeedback(id, dto);
        log.info("피드백 수정 완료 ID: {}", id);
        return ResponseEntity.ok("피드백이 수정되었습니다.");
    }

    @Operation(summary = "피드백 삭제", description = "피드백 내역을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 피드백 ID"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @DeleteMapping("/deleteFeedback/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        commandService.deleteFeedback(id);
        log.info("피드백 삭제 완료 ID: {}", id);
        return ResponseEntity.ok("피드백이 삭제되었습니다.");
    }
}