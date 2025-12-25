package com.devoops.rentalbrain.business.contract.command.service;

import com.devoops.rentalbrain.business.contract.command.dto.ContractCreateDTO;
import com.devoops.rentalbrain.business.contract.command.dto.ContractUpdateDTO;

public interface ContractCommandService {
    void updateContractStatus();
    void createContract(ContractCreateDTO  contractCreateDTO);
}
