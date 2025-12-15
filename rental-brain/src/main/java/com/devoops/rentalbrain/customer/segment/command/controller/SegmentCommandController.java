package com.devoops.rentalbrain.customer.segment.command.controller;

import com.devoops.rentalbrain.customer.segment.command.dto.SegmentCommandCreateDTO;
import com.devoops.rentalbrain.customer.segment.command.dto.SegmentCommandResponseDTO;
import com.devoops.rentalbrain.customer.segment.command.service.SegmentCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/segment")
@Slf4j
public class SegmentCommandController {
    private final SegmentCommandService segmentCommandService;

    @Autowired
    public SegmentCommandController(SegmentCommandService segmentCommandService) {
        this.segmentCommandService = segmentCommandService;
    }

    // 세그먼트 생성
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
    @DeleteMapping("/delete/{segmentId}")
    public ResponseEntity<Void> deleteSegment(@PathVariable Long segmentId) {

        log.info("채널 삭제 요청 segmentId={}", segmentId);

        segmentCommandService.deleteSegment(segmentId);

        log.info("세그먼트 삭제 완료 segmentId={}", segmentId);

        return ResponseEntity.noContent().build();
    }

}
