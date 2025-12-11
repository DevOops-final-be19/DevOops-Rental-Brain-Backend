package com.devoops.rentalbrain.employee.command.repository;

import com.devoops.rentalbrain.employee.command.entity.EmployeeAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeAuthCommandRepository extends JpaRepository<EmployeeAuth,Long> {
    List<EmployeeAuth> findByEmpId(Long empId);
}
