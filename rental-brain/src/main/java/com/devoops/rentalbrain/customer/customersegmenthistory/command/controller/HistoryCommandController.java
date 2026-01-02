package com.devoops.rentalbrain.customer.customersegmenthistory.command.controller;

import com.devoops.rentalbrain.customer.customersegmenthistory.command.dto.HistoryCommandCreateDTO;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.dto.HistoryCommandResponseDTO;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.dto.HistoryCommandUpdateDTO;
import com.devoops.rentalbrain.customer.customersegmenthistory.command.service.HistoryCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/segmenthistory")
@Tag(
        name="세그먼트 변경 이력(Command)",
        description="세그먼트 변경 이력 생성/수정/삭제 API"
)
public class HistoryCommandController {

    private final HistoryCommandService historyCommandService;

    // 세그먼트 변경 이력 생성
    @Operation(
            summary = "세그먼트 변경 이력 생성",
            description = "고객의 세그먼트 변경 이력을 신규로 생성합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "세그먼트 변경 이력 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청(입력값 오류)"),
            @ApiResponse(responseCode = "404", description = "고객 또는 세그먼트 정보 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<HistoryCommandResponseDTO> create(
            @RequestBody HistoryCommandCreateDTO dto) {
        return ResponseEntity.ok(historyCommandService.create(dto));
    }

    // 세그먼트 변경 이력 수정
    @Operation(
            summary = "세그먼트 변경 이력 수정",
            description = "historyId에 해당하는 세그먼트 변경 이력 정보를 수정합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "세그먼트 변경 이력 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청(입력값 오류)"),
            @ApiResponse(responseCode = "404", description = "변경 이력 정보 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/{id}")
    public ResponseEntity<HistoryCommandResponseDTO> update(
            @PathVariable Long id,
            @RequestBody HistoryCommandUpdateDTO dto
    ) {
        return ResponseEntity.ok(historyCommandService.update(id, dto));
    }

    // 세그먼트 변경 이력 삭제
    @Operation(
            summary = "세그먼트 변경 이력 삭제",
            description = "historyId에 해당하는 세그먼트 변경 이력을 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "세그먼트 변경 이력 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "변경 이력 정보 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        historyCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}