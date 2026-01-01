package com.devoops.rentalbrain.customer.customeranalysis.churnsnapshot.controller;

import com.devoops.rentalbrain.customer.customeranalysis.churnsnapshot.job.CustomerChurnSnapshotJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/churn-snapshot")
@RequiredArgsConstructor
public class CustomerChurnSnapshotAdminController {

    private final CustomerChurnSnapshotJobService jobService;

    @PostMapping("/run")
    public ResponseEntity<Void> run(@RequestParam("month") String month) {
        jobService.runMonthlySnapshot(month);
        return ResponseEntity.ok().build();
    }
}
