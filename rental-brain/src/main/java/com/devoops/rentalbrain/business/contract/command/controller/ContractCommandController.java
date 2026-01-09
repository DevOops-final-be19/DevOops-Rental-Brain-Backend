package com.devoops.rentalbrain.business.contract.command.controller;


import com.devoops.rentalbrain.business.contract.command.dto.ContractCreateDTO;
import com.devoops.rentalbrain.business.contract.command.service.ContractCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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


    @Operation(
            summary = "계약 해지",
            description = """
                계약을 해지 처리합니다.

                ### 처리 내용
                - 계약 상태를 **해지(T)** 로 변경합니다.
                - 해지 시점 기준 렌탈 중인 상품은 **연체(O)** 상태로 변경됩니다.
                  (※ 수리중(R) 상품은 제외)
                
                ### 제한 사항
                - 이미 종료된 계약(C, T)은 해지할 수 없습니다.
                
                ### 계약 상태 코드
                - P : 진행
                - W : 승인 대기
                - I : 만료 임박
                - T : 해지
                - C : 완료
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "계약 해지 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (이미 종료된 계약)",
                    content = @Content(
                            schema = @Schema(
                                    example = """
                                {
                                  "code": "INVALID_CONTRACT_STATUS",
                                  "message": "이미 종료된 계약입니다."
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "계약을 찾을 수 없음",
                    content = @Content(
                            schema = @Schema(
                                    example = """
                                {
                                  "code": "CONTRACT_NOT_FOUND",
                                  "message": "계약 정보를 찾을 수 없습니다."
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema())
            )
    })
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

    @Operation(
            summary = "계약 삭제 (논리 삭제)",
            description = """
            계약을 논리적으로 삭제 처리합니다.

            ### 처리 내용
            - 계약 is_deleted 값을 'Y'로 변경합니다.
            - 실제 데이터는 삭제되지 않습니다.

            ### 제한 사항
            - 이미 삭제된 계약은 삭제할 수 없습니다.
            - 종료된 계약(C, T)만 삭제할 수 있습니다.
            """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "계약 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "계약을 찾을 수 없음",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema())
            )
    })
    @PatchMapping("/{contractId}/delete")
    public ResponseEntity<Void> deleteContract(
            @PathVariable Long contractId
    ) {
        contractCommandService.deleteContract(contractId);

        log.info(
                "[API][CONTRACT_DELETE] contractId={}",
                contractId
        );

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{contractId}/delete/hard")
    public ResponseEntity<Void> deleteContractHard(@PathVariable Long contractId) {
        contractCommandService.deleteContractHard(contractId);
        return ResponseEntity.ok().build();
    }
}
