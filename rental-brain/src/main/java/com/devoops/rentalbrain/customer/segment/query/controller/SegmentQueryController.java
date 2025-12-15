package com.devoops.rentalbrain.customer.segment.query.controller;

import com.devoops.rentalbrain.customer.segment.query.dto.SegmentQueryDetailDTO;
import com.devoops.rentalbrain.customer.segment.query.dto.SegmentQueryListDTO;
import com.devoops.rentalbrain.customer.segment.query.service.SegmentQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/segment")
public class SegmentQueryController {

    private SegmentQueryService segmentQueryService;

    @Autowired
    public SegmentQueryController(SegmentQueryService segmentQueryService) {
        this.segmentQueryService = segmentQueryService;
    }

    @GetMapping("/health")
    public String health() {
        return "Segment OK";
    }

    @GetMapping("/list")
    public ResponseEntity<List<SegmentQueryListDTO>> selectSegmentList (
            @RequestParam(required = false) String segmentName
    ){
        List<SegmentQueryListDTO> list = segmentQueryService.selectSegmentList(segmentName);

        return ResponseEntity.ok(list);
    }

    @GetMapping("/list/{segmentId}")
    public ResponseEntity<SegmentQueryDetailDTO> selectSegmentDetail (
            @PathVariable Long segmentId
    ){
        SegmentQueryDetailDTO detail = segmentQueryService.selectSegmentDetail(segmentId);

        return ResponseEntity.ok(detail);
    }



}
