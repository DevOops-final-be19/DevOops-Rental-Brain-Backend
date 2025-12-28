package com.devoops.rentalbrain.common.segmentrebuild.command.controller;

import com.devoops.rentalbrain.common.segmentrebuild.command.service.SegmentRebuildBatchService;
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
    public ResponseEntity<Map<String, Object>> runOnce() {
        int u1 = segmentRebuildBatchService.fixPotentialToNew();
        int u2 = segmentRebuildBatchService.fixNewToNormalWithHistory();

        return ResponseEntity.ok(Map.of(
                "potentialToNewUpdated", u1,
                "newToNormalUpdated", u2
        ));
    }
}
