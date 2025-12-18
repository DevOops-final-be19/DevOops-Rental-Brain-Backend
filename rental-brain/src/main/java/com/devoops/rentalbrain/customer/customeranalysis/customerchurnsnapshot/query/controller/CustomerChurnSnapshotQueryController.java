package com.devoops.rentalbrain.customer.customeranalysis.customerchurnsnapshot.query.controller;

import com.devoops.rentalbrain.customer.customeranalysis.customerchurnsnapshot.query.dto.ChurnKpiCardResponseDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customerchurnsnapshot.query.dto.MonthlyRiskRateResponseDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customerchurnsnapshot.query.service.CustomerChurnSnapshotQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customer-analysis/churn-snapshot")

public class CustomerChurnSnapshotQueryController {

    private final CustomerChurnSnapshotQueryService customerChurnSnapshotQueryService;


    @Autowired
    public CustomerChurnSnapshotQueryController(CustomerChurnSnapshotQueryService customerChurnSnapshotQueryService) {
        this.customerChurnSnapshotQueryService = customerChurnSnapshotQueryService;
    }

    @GetMapping("/health")
    public String health() {
        return "risk OK";
    }

    // 이탈률 Kpi 카드 조회
    @GetMapping("/kpi-card")
    public ResponseEntity<ChurnKpiCardResponseDTO> kpiCard(@RequestParam("month") String month) {

        ChurnKpiCardResponseDTO kpiCard = customerChurnSnapshotQueryService.getKpiCard(month);

        return ResponseEntity.ok(kpiCard);
    }

    // 차트 (월별 위험률 차트)
    @GetMapping("/monthly-rate")
    public ResponseEntity<List<MonthlyRiskRateResponseDTO>> monthlyRate(
            @RequestParam("from") String fromMonth,
            @RequestParam("to") String toMonth
    ){
        List<MonthlyRiskRateResponseDTO> list
                = customerChurnSnapshotQueryService.getMonthlyRiskRate(fromMonth, toMonth);

        return ResponseEntity.ok(list);
    }


}
