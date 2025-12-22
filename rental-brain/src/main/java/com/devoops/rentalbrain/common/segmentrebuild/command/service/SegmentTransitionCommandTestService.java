package com.devoops.rentalbrain.common.segmentrebuild.command.service;

import com.devoops.rentalbrain.customer.customerlist.command.repository.CustomerlistCommandRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class SegmentTransitionCommandTestService {

    private final CustomerlistCommandRepository customerlistCommandRepository;

    @Autowired
    public SegmentTransitionCommandTestService(CustomerlistCommandRepository customerlistCommandRepository) {
        this.customerlistCommandRepository = customerlistCommandRepository;
    }
    @Transactional
    public int fixPotentialToNew() {


        return customerlistCommandRepository.bulkPromotePotentialToNew();
    }

}
