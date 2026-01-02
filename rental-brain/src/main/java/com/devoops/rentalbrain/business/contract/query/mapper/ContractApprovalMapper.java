package com.devoops.rentalbrain.business.contract.query.mapper;

import com.devoops.rentalbrain.business.contract.query.dto.CustomerContractApprovalDTO;
import com.devoops.rentalbrain.business.contract.query.dto.EmpContractDTO;
import com.devoops.rentalbrain.common.pagination.Criteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContractApprovalMapper {
    List<CustomerContractApprovalDTO> CustomerContractList(
            @Param("criteria") Criteria criteria
    );

    long CustomerContractListCount(
            @Param("criteria") Criteria criteria
    );

    List<EmpContractDTO> getContractEmpList();
}
