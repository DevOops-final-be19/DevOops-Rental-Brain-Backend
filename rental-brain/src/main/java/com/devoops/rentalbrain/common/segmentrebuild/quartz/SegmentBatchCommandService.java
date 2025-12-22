package com.devoops.rentalbrain.common.segmentrebuild.quartz;


import com.devoops.rentalbrain.customer.customerlist.command.repository.CustomerlistCommandRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SegmentBatchCommandService {
    private final CustomerlistCommandRepository customerlistCommandRepository;


    //insertHistoryNewToNormal -> 이력남기는것


    // 이건 이벤트로 처리해서 이력 알아서 남음
    @Transactional
    public int fixPotentialToNew() {
        return customerlistCommandRepository.bulkPromotePotentialToNew();
    }

    @Transactional
    public int promoteNewToNormal() {
        customerlistCommandRepository.insertHistoryNewToNormal();
        return customerlistCommandRepository.bulkPromoteNewToNormal();
    }
}
