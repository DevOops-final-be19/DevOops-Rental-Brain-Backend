package com.devoops.rentalbrain.product.maintenance.command.repository;

import com.devoops.rentalbrain.product.maintenance.command.entity.AfterService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AfterServiceRepository extends JpaRepository<AfterService, Long> {

}