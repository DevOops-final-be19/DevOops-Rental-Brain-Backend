package com.devoops.rentalbrain.business.contract.command.service;

import com.devoops.rentalbrain.business.contract.command.dto.ContractCreateDTO;
import com.devoops.rentalbrain.business.contract.command.repository.ContractCommandRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class ContractCommandServiceImpl implements ContractCommandService {

    private final ContractCommandRepository contractCommandRepository;
    private final ModelMapper modelMapper;
    @Autowired
    public ContractCommandServiceImpl(ContractCommandRepository contractCommandRepository,
                                      ModelMapper modelMapper) {
        this.contractCommandRepository = contractCommandRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Scheduled(cron = "0 0 1 * * *")
    @Transactional
    public void updateContractStatus() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMonthLater = now.plusMonths(1);
        LocalDateTime twoMonthsLater = now.plusMonths(2);

        int expected =
                contractCommandRepository.updateToExpireExpected(
                        oneMonthLater, twoMonthsLater
                );

        int imminent =
                contractCommandRepository.updateToExpireImminent(
                        now, oneMonthLater
                );

        int closed =
                contractCommandRepository.updateToClosed(now);

        log.info(
                "[계약 상태 스케줄러] 만료예정(E): {}, 만료임박(I): {}, 계약만료(C): {}",
                expected, imminent, closed
        );
    }

    @Override
    public void createContract(ContractCreateDTO contractCreateDTO) {

    }


}
