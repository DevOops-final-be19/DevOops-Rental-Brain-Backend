package com.devoops.rentalbrain.customer.segment.command.controller;

import com.devoops.rentalbrain.customer.segment.command.dto.SegmentCommandCreateDTO;
import com.devoops.rentalbrain.customer.segment.command.dto.SegmentCommandResponseDTO;
import com.devoops.rentalbrain.customer.segment.command.service.SegmentCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/segment")
@Slf4j
@Tag(
        name = "세그먼트 관리(Command)",
        description = "고객 세그먼트 등록, 수정, 삭제 등 세그먼트 관련 명령(Command) API"
)
public class SegmentCommandController {
    private final SegmentCommandService segmentCommandService;

    @Autowired
    public SegmentCommandController(SegmentCommandService segmentCommandService) {
        this.segmentCommandService = segmentCommandService;
    }

    // 세그먼트 생성
    @Operation(
            summary = "세그먼트 등록",
            description = "신규 고객 세그먼트를 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "세그먼트 등록 성공"),
            @ApiResponse(responseCode = "400", description = "요청 값 검증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/insert")
    public ResponseEntity<SegmentCommandCreateDTO> insertSegment(
            @RequestBody SegmentCommandCreateDTO segmentCommandCreateDTO) {

        // insert 할 DTO들 저장하는 것
        SegmentCommandCreateDTO saved
                = segmentCommandService.insertSegment(segmentCommandCreateDTO);

        if (saved == null) {
            log.info("세그먼트 저장 실패: {}", segmentCommandCreateDTO);
            throw new IllegalArgumentException("채널 저장 실패했습니다.");
        } else {
            log.info("세그먼트 저장 완료!: {}", saved);
            return ResponseEntity.ok().body(saved);
        }
    }

    // 세그먼트 수정
    @Operation(
            summary = "세그먼트 수정",
            description = "segmentId에 해당하는 세그먼트 정보를 수정합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "세그먼트 수정 성공"),
            @ApiResponse(responseCode = "400", description = "요청 값 검증 실패"),
            @ApiResponse(responseCode = "404", description = "세그먼트 정보 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/update/{segmentId}")
    public ResponseEntity<SegmentCommandResponseDTO> updateSegment(
            @PathVariable Long segmentId,
            @RequestBody SegmentCommandResponseDTO segmentCommandResponseDTO) {

        log.info("세그먼트 수정 요청 segmentId={}, newName={}",
                segmentId, segmentCommandResponseDTO.getSegmentName());

        SegmentCommandResponseDTO updated = segmentCommandService.updateSegment(segmentId, segmentCommandResponseDTO);

        log.info("세그먼트 수정 완료 segmentId={}", segmentId);

        return ResponseEntity.ok(updated);
    }

    // 세그먼트 삭제
    @Operation(
            summary = "세그먼트 삭제",
            description = "segmentId에 해당하는 세그먼트 정보를 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "세그먼트 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "세그먼트 정보 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/delete/{segmentId}")
    public ResponseEntity<Void> deleteSegment(@PathVariable Long segmentId) {

        log.info("채널 삭제 요청 segmentId={}", segmentId);

        segmentCommandService.deleteSegment(segmentId);

        log.info("세그먼트 삭제 완료 segmentId={}", segmentId);

        return ResponseEntity.noContent().build();
    }

}
