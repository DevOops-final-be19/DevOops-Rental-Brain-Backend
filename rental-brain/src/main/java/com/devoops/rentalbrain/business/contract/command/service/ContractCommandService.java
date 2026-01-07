package com.devoops.rentalbrain.business.contract.command.service;

import com.devoops.rentalbrain.business.contract.command.dto.ContractCreateDTO;
import com.devoops.rentalbrain.business.contract.command.dto.ContractUpdateDTO;

public interface ContractCommandService {
    void createContract(ContractCreateDTO  contractCreateDTO);
    void terminateContract(Long contractId);
    void deleteContract(Long contractId);

    void deleteContractHard(Long contractId);
}
