package com.devoops.rentalbrain.customer.customeranalysis.churnsnapshot.job;

import com.devoops.rentalbrain.customer.customeranalysis.churnsnapshot.mapper.CustomerChurnSnapshotMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.YearMonth;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerChurnSnapshotJobService {

    private final CustomerChurnSnapshotMapper mapper;

    @Transactional
    public void runMonthlySnapshot(String month) {
        int affected = mapper.upsertMonthlySnapshot(month);
        int custCnt = mapper.countDistinctCustomersByMonth(month);
        int riskCnt = mapper.countRiskCustomersByMonth(month);

        log.info("[ChurnSnapshot] month={}, upsertRows={}, custCnt={}, riskCnt={}",
                month, affected, custCnt, riskCnt);
    }

    @Transactional
    public void runPrevMonthSnapshot() {
        runMonthlySnapshot(YearMonth.now().minusMonths(1).toString());
    }
}
