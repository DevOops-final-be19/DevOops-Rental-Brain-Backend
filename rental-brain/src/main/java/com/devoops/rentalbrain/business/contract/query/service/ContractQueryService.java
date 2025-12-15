package com.devoops.rentalbrain.business.contract.query.service;

import com.devoops.rentalbrain.business.contract.query.dto.AllContractDTO;
import com.devoops.rentalbrain.business.contract.query.dto.ContractSearchDTO;
import com.devoops.rentalbrain.business.contract.query.dto.ContractSummaryDTO;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;

import java.util.List;

public interface ContractQueryService {
    PageResponseDTO<AllContractDTO> getContractListWithPaging(ContractSearchDTO criteria);
    ContractSummaryDTO getContractSummary();
}
