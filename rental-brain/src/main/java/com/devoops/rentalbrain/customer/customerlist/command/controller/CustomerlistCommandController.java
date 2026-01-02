package com.devoops.rentalbrain.customer.customerlist.command.controller;

import com.devoops.rentalbrain.customer.customerlist.command.dto.CustomerlistCommandDTO;
import com.devoops.rentalbrain.customer.customerlist.command.service.CustomerlistCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "고객 관리(Command)",
        description = "고객 등록, 수정, 삭제(Soft/Restore) 등 고객 관련 명령(Command) API"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
@Slf4j
public class CustomerlistCommandController {

    private final CustomerlistCommandService commandService;

    @Operation(summary = "고객 등록", description = "신규 고객 정보를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/insertCustomer")
    public ResponseEntity<Long> register(@RequestBody CustomerlistCommandDTO dto) {
        Long savedId = commandService.registerCustomer(dto);
        log.info("고객 등록 완료 ID: {}", savedId);
        return ResponseEntity.ok(savedId);
    }

    @Operation(summary = "고객 정보 수정", description = "기존 고객의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 고객 ID"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PutMapping("/updateCustomer/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody CustomerlistCommandDTO dto) {
        commandService.updateCustomer(id, dto);
        log.info("고객 수정 완료 ID: {}", id);
        return ResponseEntity.ok("고객 정보가 수정되었습니다.");
    }

    @Operation(summary = "고객 삭제 (Soft Delete)", description = "고객을 삭제 처리(Soft Delete, isDeleted=Y)합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 고객 ID"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @DeleteMapping("/deleteCustomer/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        commandService.deleteCustomer(id);
        log.info("고객 삭제(Soft Delete) 완료 ID: {}", id);
        return ResponseEntity.ok("삭제되었습니다.");
    }

    @Operation(summary = "고객 복구 (Restore)", description = "삭제된 고객을 복구(isDeleted=N)합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "복구 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 고객 ID"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PutMapping("/restoreCustomer/{id}")
    public ResponseEntity<String> restore(@PathVariable Long id) {
        commandService.restoreCustomer(id);
        log.info("고객 복구 요청 완료 ID: {}", id);
        return ResponseEntity.ok("고객이 정상적으로 복구되었습니다.");
    }
}