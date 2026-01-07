package com.devoops.rentalbrain.common.segmentrebuild.command.controller;

import com.devoops.rentalbrain.common.segmentrebuild.command.service.SegmentRebuildBatchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/segmentrebuild")
public class SegmentRebuildTestController {

    private final SegmentRebuildBatchService segmentRebuildBatchService;

    @PostMapping("/run")
    @Operation(
            summary = "세그먼트 배치 수동 실행 (우선순위 + 단일 이동 보장)",
            description = """
                고객 세그먼트 자동 보정 배치를 수동으로 1회 실행합니다.

                특징:
                - 우선순위 정책 적용
                - 한 배치 실행당 고객은 최대 1번만 이동
                - 위험/블랙 > 승격 > 확장 > 복귀 순서 보장

                실제 Quartz 배치와 동일한 로직을 실행합니다.
                """
    )
    public ResponseEntity<Map<String, Integer>> runOnce() {

        //  Quartz Job과 동일한 실행 경로
        Map<String, Integer> result =
                segmentRebuildBatchService.runWithPriorityOnce();

        return ResponseEntity.ok(result);
    }
}
