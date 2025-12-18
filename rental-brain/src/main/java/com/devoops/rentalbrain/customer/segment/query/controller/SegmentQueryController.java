package com.devoops.rentalbrain.customer.segment.query.controller;

import com.devoops.rentalbrain.customer.segment.query.dto.SegmentQueryDetailDTO;
import com.devoops.rentalbrain.customer.segment.query.dto.SegmentQueryListDTO;
import com.devoops.rentalbrain.customer.segment.query.service.SegmentQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/segment")
@Tag(
        name = "세그먼트 조회(Query)",
        description = "고객 세그먼트 목록 조회 및 상세 조회 API"
)
public class SegmentQueryController {

    private final SegmentQueryService segmentQueryService;

    @Autowired
    public SegmentQueryController(SegmentQueryService segmentQueryService) {
        this.segmentQueryService = segmentQueryService;
    }

    @GetMapping("/health")
    public String health() {
        return "Segment OK";
    }

    @Operation(
            summary = "세그먼트 목록 조회",
            description = "세그먼트 목록을 조회합니다. 세그먼트명으로 검색할 수 있습니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "세그먼트 목록 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/list")
    public ResponseEntity<List<SegmentQueryListDTO>> selectSegmentList (
            @RequestParam(required = false) String segmentName
    ){
        List<SegmentQueryListDTO> list = segmentQueryService.selectSegmentList(segmentName);

        return ResponseEntity.ok(list);
    }

    @Operation(
            summary = "세그먼트 상세 조회",
            description = "segmentId에 해당하는 세그먼트 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "세그먼트 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "세그먼트 정보 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/list/{segmentId}")
    public ResponseEntity<SegmentQueryDetailDTO> selectSegmentDetail (
            @PathVariable Long segmentId
    ){
        SegmentQueryDetailDTO detail = segmentQueryService.selectSegmentDetail(segmentId);

        return ResponseEntity.ok(detail);
    }



}
