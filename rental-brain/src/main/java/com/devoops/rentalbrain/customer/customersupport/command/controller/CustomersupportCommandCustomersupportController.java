package com.devoops.rentalbrain.customer.customersupport.command.controller;

import com.devoops.rentalbrain.customer.customersupport.command.dto.CustomersupportDTO;
import com.devoops.rentalbrain.customer.customersupport.command.service.CustomersupportCommandCustomersupportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "고객 문의 관리(Command)",
        description = "문의 등록, 수정, 삭제 등 고객 문의 관련 명령(Command) API"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/customersupports")
@Slf4j
public class CustomersupportCommandCustomersupportController {

    private final CustomersupportCommandCustomersupportService commandService;

    @Operation(summary = "문의 등록", description = "신규 고객 문의를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/insertSupport")
    public ResponseEntity<String> register(@RequestBody CustomersupportDTO dto) {
        Long savedId = commandService.registerSupport(dto);
        log.info("문의 등록 완료 ID: {}", savedId);
        return ResponseEntity.ok("문의가 정상적으로 등록되었습니다. (ID: " + savedId + ")");
    }

    @Operation(summary = "문의 수정", description = "기존 문의 내역을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 문의 ID"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PutMapping("/updateSupport/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody CustomersupportDTO dto) {
        commandService.updateSupport(id, dto);
        log.info("문의 수정 완료 ID: {}", id);
        return ResponseEntity.ok("문의가 수정되었습니다.");
    }

    @Operation(summary = "문의 삭제", description = "문의 내역을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 문의 ID"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @DeleteMapping("/deleteSupport/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        commandService.deleteSupport(id);
        log.info("문의 삭제 완료 ID: {}", id);
        return ResponseEntity.ok("문의가 삭제되었습니다.");
    }
}