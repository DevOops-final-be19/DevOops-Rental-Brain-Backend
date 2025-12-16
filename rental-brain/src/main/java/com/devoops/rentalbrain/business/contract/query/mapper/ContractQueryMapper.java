package com.devoops.rentalbrain.business.contract.query.mapper;

import com.devoops.rentalbrain.business.contract.query.dto.AllContractDTO;
import com.devoops.rentalbrain.business.contract.query.dto.ContractSearchDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContractQueryMapper {
    List<AllContractDTO> getContractList(ContractSearchDTO contractSearchDTO);
    long getCountContract(ContractSearchDTO contractSearchDTO);

    long countAllContracts();
    long countProgressContracts();
    long countExpectedExpireContracts();
    long countImminentExpireContracts();
    long countThisMonthContracts();
}
