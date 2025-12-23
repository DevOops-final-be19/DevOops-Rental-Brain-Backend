package com.devoops.rentalbrain.employee.command.repository;

import com.devoops.rentalbrain.employee.command.entity.EmpPosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpPositionCommandRepository extends JpaRepository<EmpPosition,Long> {
}
