package com.devoops.rentalbrain.product.maintenance.command.controller;

import com.devoops.rentalbrain.product.maintenance.command.dto.*;
import com.devoops.rentalbrain.product.maintenance.command.service.AfterServiceCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/as")
@Tag(name = "AS / 정기점검 관리", description = "AS 및 정기점검 생성 · 수정(Command) API" )
public class AfterServiceCommandController {

    private final AfterServiceCommandService service;

    @Operation(
            summary = "AS / 정기점검 생성",
            description = """
                    AS 또는 정기점검 일정을 생성한다.
                    
                    - type은 A(AS) 또는 R(정기점검)만 가능
                    - 동일 자산(itemId) + 동일 예정일(dueDate)의 일정은 중복 생성 불가
                    - 상태(status)는 기본값으로 'P(예정)' 처리됨
                    - 업무 코드(after_service_code)는 서버에서 자동 생성됨
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "생성 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (필수 값 누락, type 오류)",
                    content = @Content(schema = @Schema(example = """
                        {
                          "code": "AS001",
                          "message": "type은 A(AS) 또는 R(정기점검)만 가능합니다."
                        }
                    """))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "중복 일정 존재",
                    content = @Content(schema = @Schema(example = """
                        {
                          "code": "AS002",
                          "message": "이미 동일 자산/동일 예정일의 점검 일정이 존재합니다."
                        }
                    """))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류"
            )
    })
    @PostMapping
    public void create(@RequestBody AfterServiceCreateRequest request) {
        service.create(request);
    }

    @Operation(
            summary = "AS / 정기점검 수정",
            description = """
                    AS 또는 정기점검 정보를 수정한다.
                    
                    - 존재하지 않는 asId로 요청 시 오류 발생
                    - 수정 요청 시 type은 A(AS) 또는 R(정기점검)만 허용
                    - 전달된 필드만 부분 수정 처리됨 (PATCH 방식과 유사)
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "수정 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (type 값 오류 등)",
                    content = @Content(schema = @Schema(example = """
                        {
                          "code": "AS001",
                          "message": "type은 A(AS) 또는 R(정기점검)만 가능합니다."
                        }
                    """))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "AS / 정기점검 정보 없음",
                    content = @Content(schema = @Schema(example = """
                        {
                          "code": "AS003",
                          "message": "수정할 AS/정기점검이 존재하지 않습니다."
                        }
                    """))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류"
            )
    })
    @PutMapping("/{asId}")
    public void update(
            @PathVariable Long asId,
            @RequestBody AfterServiceUpdateRequest request
    ) {
        service.update(asId, request);
    }

    @Operation(
            summary = "AS / 정기점검 삭제",
            description = """
                AS 또는 정기점검 일정을 삭제한다.
                
                - 존재하지 않는 asId로 요청 시 오류 발생
                - 삭제는 물리 삭제 방식으로 처리됨
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "AS / 정기점검 정보 없음",
                    content = @Content(schema = @Schema(example = """
                    {
                      "code": "AS003",
                      "message": "삭제할 AS/정기점검이 존재하지 않습니다."
                    }
                """))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류"
            )
    })
    @DeleteMapping("/{asId}")
    public void delete(@PathVariable Long asId) {
        service
                .delete(asId);
    }

}