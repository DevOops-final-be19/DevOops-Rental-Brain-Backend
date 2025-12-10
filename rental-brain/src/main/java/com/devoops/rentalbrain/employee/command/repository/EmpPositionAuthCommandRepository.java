package com.devoops.rentalbrain.employee.command.repository;

import com.devoops.rentalbrain.employee.command.entity.EmpPositionAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpPositionAuthCommandRepository extends JpaRepository<EmpPositionAuth,Long> {
}
