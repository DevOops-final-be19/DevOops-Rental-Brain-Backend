package com.devoops.rentalbrain.product.maintenance.query.controller;

import com.devoops.rentalbrain.product.maintenance.query.dto.AfterServiceDetailDTO;
import com.devoops.rentalbrain.product.maintenance.query.dto.AfterServiceSearchDTO;
import com.devoops.rentalbrain.product.maintenance.query.dto.AfterServiceSummaryDTO;
import com.devoops.rentalbrain.product.maintenance.query.dto.NextWeekScheduleDTO;
import com.devoops.rentalbrain.product.maintenance.query.service.AfterServiceQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/as")
@Tag(name = "AS / 정기점검 조회", description = "A/S 및 정기점검 조회 관련 API")
public class AfterServiceQueryController {

    private final AfterServiceQueryService afterServiceQueryService;

    @Operation(
            summary = "AS / 정기점검 전체 목록 조회",
            description = "등록된 모든 AS 및 정기점검 목록을 조회한다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public AfterServiceSearchDTO findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String summaryType,
            @RequestParam(required = false, defaultValue = "dueDate") String sortField,
            @RequestParam(required = false, defaultValue = "DESC") String sortOrder
    ) {
        return afterServiceQueryService.findAll(page, size, type, status, keyword, summaryType, sortField, sortOrder);
    }

    @Operation(
            summary = "AS / 정기점검 상세 조회",
            description = "AS ID를 기준으로 상세 정보를 조회한다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 AS 정보 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{asId}")
    public AfterServiceDetailDTO findDetail(@PathVariable Long asId) {
        return afterServiceQueryService.findById(asId);
    }

    @Operation(
            summary = "AS / 정기점검 요약 정보 조회",
            description = """
            대시보드 상단에 표시되는 요약 정보 조회
            - 이번 달 예정 점검 수
            - 72시간 이내 임박 점검 수
            - 이번 달 완료 점검 수
            - 진행 중 AS / 점검 수
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/summary")
    public AfterServiceSummaryDTO summary() {
        return afterServiceQueryService.getSummary();
    }

    @Operation(
            summary = "다음 주 점검 예정 목록 조회",
            description = "다음 주에 예정된 AS / 정기점검 목록을 조회한다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/upcoming")
    public List<NextWeekScheduleDTO> nextWeekList() {
        return afterServiceQueryService.findNextWeekList();
    }

    @Operation(
            summary = "다음 주 점검 예정 건수 조회",
            description = "다음 주에 예정된 AS / 정기점검 총 건수를 조회한다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(example = "5"))
            ),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/upcoming/count")
    public int nextWeekCount() {
        return afterServiceQueryService.countNextWeek();
    }
}
