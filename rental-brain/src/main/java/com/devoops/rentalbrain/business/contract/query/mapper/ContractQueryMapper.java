package com.devoops.rentalbrain.business.contract.query.mapper;

import com.devoops.rentalbrain.business.contract.query.dto.AllContractDTO;
import com.devoops.rentalbrain.business.contract.query.dto.ContractSearchDTO;
import com.devoops.rentalbrain.business.contract.query.dto.EmpContractDTO;
import com.devoops.rentalbrain.business.contract.query.dto.RentalProductInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContractQueryMapper {
    List<AllContractDTO> getContractList(ContractSearchDTO contractSearchDTO);
    long getCountContract(ContractSearchDTO contractSearchDTO);
    long countAllContracts();
    long countProgressContracts();
    long countImminentExpireContracts();
    long countThisMonthContracts();
    List<RentalProductInfoDTO> selectRentalProductList(@Param("contractId") long contractId);

}
