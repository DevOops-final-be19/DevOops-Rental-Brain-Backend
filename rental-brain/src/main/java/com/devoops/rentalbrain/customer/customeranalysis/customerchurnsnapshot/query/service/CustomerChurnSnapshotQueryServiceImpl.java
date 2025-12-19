package com.devoops.rentalbrain.customer.customeranalysis.customerchurnsnapshot.query.service;

import com.devoops.rentalbrain.customer.customeranalysis.customerchurnsnapshot.query.dto.ChurnKpiCardResponseDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customerchurnsnapshot.query.dto.MonthlyRiskRateResponseDTO;
import com.devoops.rentalbrain.customer.customeranalysis.customerchurnsnapshot.query.mapper.CustomerChurnSnapshotQueryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CustomerChurnSnapshotQueryServiceImpl implements CustomerChurnSnapshotQueryService{

    private final CustomerChurnSnapshotQueryMapper customerChurnSnapshotQueryMapper;

    @Autowired
    public CustomerChurnSnapshotQueryServiceImpl(CustomerChurnSnapshotQueryMapper customerChurnSnapshotQueryMapper) {
        this.customerChurnSnapshotQueryMapper = customerChurnSnapshotQueryMapper;
    }

    // Kpi 카드 조회
    @Override
    public ChurnKpiCardResponseDTO getKpiCard(String month) {

        // 전체 수 조회
        int total = customerChurnSnapshotQueryMapper.countTotalCustomers();


        YearMonth yearMonth = YearMonth.parse(month);
        String prevMonth = yearMonth.minusMonths(1).toString();

        int curRisk = customerChurnSnapshotQueryMapper.countMonthRiskCustomers(month);
        int prevRisk = customerChurnSnapshotQueryMapper.countMonthRiskCustomers(prevMonth);

        double curRiskRate = rate(curRisk, total);
        double prevRiskRate = rate(prevRisk, total);

        int retained = Math.max(total - curRisk, 0);
        double retentionRate = round1(100.0 - curRiskRate);

        return ChurnKpiCardResponseDTO.builder()
                                      .snapshotMonth(month)
                                      .prevMonth(prevMonth)
                                      .totalCustomerCount(total)
                                      .curRiskCustomerCount(curRisk)
                                      .curRiskRate(curRiskRate)
                                      .prevRiskCustomerCount(prevRisk)
                                      .prevRiskRate(prevRiskRate)
                                      .momDiffRate(round1(curRiskRate - prevRiskRate)) // %p
                                      .curRetainedCustomerCount(retained)
                                      .curRetentionRate(retentionRate)
                                      .build();
    }

    private double rate(int numerator, int denom) {
        if (denom <= 0) return 0.0;
        return round1((double) numerator / denom * 100.0);
    }

    private double round1(double v) {
        return Math.round(v * 10.0) / 10.0;
    }


    // 차트 (월별 위험률 차트)
    @Override
    public List<MonthlyRiskRateResponseDTO> getMonthlyRiskRate(String fromMonth, String toMonth) {
        int total =  customerChurnSnapshotQueryMapper.countTotalCustomers();

        List<String> months = customerChurnSnapshotQueryMapper.findMonthsBetween(fromMonth, toMonth);
        List<MonthlyRiskRateResponseDTO> result = new ArrayList<>();

        for(String month : months) {
            int risk = customerChurnSnapshotQueryMapper.countMonthRiskCustomers(month);

            result.add(MonthlyRiskRateResponseDTO.builder()
                    .snapshotMonth(month)
                    .riskCustomerCount(risk)
                    .riskRate(rate(risk,total))
                    .build());
        }
        return result;
    }


}
