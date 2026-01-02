package com.devoops.rentalbrain.business.contract.command.repository;

import com.devoops.rentalbrain.business.contract.command.entity.ContractItemCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractItemCommandRepository extends JpaRepository<ContractItemCommandEntity, Long> {

}
