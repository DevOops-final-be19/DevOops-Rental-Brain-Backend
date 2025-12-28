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
    public ResponseEntity<Map<String, Object>> runOnce() {
        int u1 = segmentRebuildBatchService.fixPotentialToNew();
        int u2 = segmentRebuildBatchService.fixNewToNormalWithHistory();
        int u3 = segmentRebuildBatchService.fixNormalToVipWithHistory();

        // 위험/블랙 우선
        int u4 = segmentRebuildBatchService.fixToRiskWithHistory();
        int u5 = segmentRebuildBatchService.fixRiskToBlacklistWithHistory();

        // 확장 의사 고객
        int u6 = segmentRebuildBatchService.fixToExpansionWithHistory();
        int u7 = segmentRebuildBatchService.fixRiskToNormalWithHistory();

        return ResponseEntity.ok(Map.of(
                "potentialToNewUpdated", u1,
                "newToNormalUpdated", u2,
                "normalToVipUpdated", u3,
                "toRiskUpdated", u4,
                "riskToBlacklistUpdated", u5,
                "toExpansionUpdated", u6,
                "riskToNormal", u7
        ));
    }
}
