package com.devoops.rentalbrain.business.contract.query.service;

import com.devoops.rentalbrain.business.contract.query.dto.*;
import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;

import java.util.List;

public interface ContractQueryService {
    PageResponseDTO<AllContractDTO> getContractListWithPaging(ContractSearchDTO criteria);
    ContractSummaryDTO getContractSummary();
    ContractBasicInfoDTO getContractBasicInfo(Long contractId);
    ContractItemInfoDTO getContractItemInfo(Long contractId);
    List<ContractPaymentDTO> getContractPayments(Long contractId);
    List<RentalProductInfoDTO> getRentalProductList(Long contractId);
    PageResponseDTO<CustomerContractApprovalDTO> getCustomerContractApprovalList(Criteria criteria);
    List<EmpContractDTO> getContractEmpList();
}
