package com.devoops.rentalbrain.business.contract.command.controller;


import com.devoops.rentalbrain.business.contract.command.dto.ContractCreateDTO;
import com.devoops.rentalbrain.business.contract.command.service.ContractCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "계약관리(Command)", description = "계약 생성 API")
public class ContractCommandController {

    private final ContractCommandService contractCommandService;

    @Operation(
            summary = "계약 생성",
            description = """
                    새로운 계약을 생성합니다.
                    
                    - 고객 정보
                    - 계약 기간
                    - 계약 상품 목록
                    - 결제 정보
                    를 포함하여 계약을 생성합니다.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "계약 생성 성공"
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청",
            content = @Content(schema = @Schema())
    )
    @ApiResponse(
            responseCode = "500",
            description = "서버 오류",
            content = @Content(schema = @Schema())
    )
    @PostMapping
    public ResponseEntity<Void> createContract(
            @RequestBody ContractCreateDTO dto
    ) {
        log.info("[계약 생성 요청] {}", dto);

        contractCommandService.createContract(dto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{contractId}/terminate")
    public ResponseEntity<Void> terminateContract(
            @PathVariable Long contractId
    ) {

        contractCommandService.terminateContract(contractId);

        log.info(
                "[API][CONTRACT_TERMINATE] contractId={}",
                contractId
        );

        return ResponseEntity.ok().build();
    }
}
