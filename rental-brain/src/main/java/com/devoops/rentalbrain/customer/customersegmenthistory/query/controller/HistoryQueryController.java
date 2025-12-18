package com.devoops.rentalbrain.customer.customersegmenthistory.query.controller;

import com.devoops.rentalbrain.customer.customersegmenthistory.query.dto.HistoryListQueryDTO;
import com.devoops.rentalbrain.customer.customersegmenthistory.query.dto.HistoryQueryDTO;
import com.devoops.rentalbrain.customer.customersegmenthistory.query.service.HistoryQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/segmenthistory")
@Tag(
        name = "세그먼트 변경 이력 조회(Query)",
        description = "고객 세그먼트 변경 이력 조회 및 상세 조회 API"
)
public class HistoryQueryController {

    private final HistoryQueryService historyQueryService;


    @Autowired
    public HistoryQueryController(HistoryQueryService historyQueryService) {
        this.historyQueryService = historyQueryService;
    }

    @GetMapping("/health")
    public String health() {
        return "SegmentHistory OK";
    }

    // 세그먼트 변경 이력 list 조회
    @Operation(
            summary = "고객별 세그먼트 변경 이력 목록 조회",
            description = "customerId에 해당하는 고객의 세그먼트 변경 이력 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "세그먼트 변경 이력 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청(필수 파라미터 누락/형식 오류)"),
            @ApiResponse(responseCode = "404", description = "고객 또는 변경 이력 정보 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })

    // http://localhost:8080/segmenthistory?customerId=12
    @GetMapping
    public ResponseEntity<HistoryListQueryDTO> list(
            @RequestParam Long customerId) {
        HistoryListQueryDTO list = historyQueryService.getByCustomer(customerId);

        return ResponseEntity.ok(list);
    }

    // 세그먼트 변경 이력 detail 조회
    @Operation(
            summary = "세그먼트 변경 이력 상세 조회",
            description = "historyId에 해당하는 세그먼트 변경 이력 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "세그먼트 변경 이력 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "변경 이력 정보 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{historyId}")
    public ResponseEntity<HistoryQueryDTO> getHistory(@PathVariable Long historyId) {

        HistoryQueryDTO detail = historyQueryService.getHistory(historyId);

        return ResponseEntity.ok(detail);
    }


}
