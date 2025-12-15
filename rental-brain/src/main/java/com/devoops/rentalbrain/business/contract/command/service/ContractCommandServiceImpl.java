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
    public ContractCommandServiceImpl(
            ContractCommandRepository contractCommandRepository,
            ModelMapper modelMapper
    ) {
        this.contractCommandRepository = contractCommandRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * 계약 상태 자동 변경 스케줄러
     *
     * 상태 흐름:
     * P(진행중) → I(만료임박, 1개월 전) → C(계약만료)
     */
    @Override
    @Scheduled(cron = "0 0 1 * * *") // 매일 새벽 1시
    @Transactional
    public void updateContractStatus() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMonthLater = now.plusMonths(1);

        int imminent =
                contractCommandRepository.updateToExpireImminent(
                        now, oneMonthLater
                );

        int closed =
                contractCommandRepository.updateToClosed(now);

        log.info(
                "[계약 상태 스케줄러] 만료임박(I): {}, 계약만료(C): {}",
                imminent, closed
        );
    }

    @Override
    public void createContract(ContractCreateDTO contractCreateDTO) {
        // TODO: 계약 생성 로직
        // 1. DTO → Entity 매핑
        // 2. 초기 상태 P 설정
        // 3. 저장
    }
}
