package com.devoops.rentalbrain.business.contract.command.service;

import com.devoops.rentalbrain.business.contract.command.dto.ContractCreateDTO;

public interface ContractCommandService {
    void updateContractStatus();
    void createContract(ContractCreateDTO  contractCreateDTO);
}
