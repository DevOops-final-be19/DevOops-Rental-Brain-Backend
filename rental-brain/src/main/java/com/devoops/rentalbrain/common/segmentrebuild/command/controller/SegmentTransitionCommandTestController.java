package com.devoops.rentalbrain.common.segmentrebuild.command.controller;

import com.devoops.rentalbrain.common.segmentrebuild.command.service.SegmentTransitionCommandTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/segment/quartz")
@Slf4j
public class SegmentTransitionCommandTestController {

    private final SegmentTransitionCommandTestService segmentTransitionCommandTestService;


    @Autowired
    public SegmentTransitionCommandTestController(SegmentTransitionCommandTestService segmentTransitionCommandTestService) {
        this.segmentTransitionCommandTestService = segmentTransitionCommandTestService;
    }


    @PostMapping("/fixpotential")
    public ResponseEntity<String> fixPotentialToNew(){
        int update = segmentTransitionCommandTestService.fixPotentialToNew();

        return ResponseEntity.ok("update= " +  update);
    }
}
