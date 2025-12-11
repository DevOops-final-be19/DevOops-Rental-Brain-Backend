package com.devoops.rentalbrain.employee.command.repository;

import com.devoops.rentalbrain.employee.command.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeCommandRepository extends JpaRepository<Employee,Long> {
    Employee findByEmpId(String empId);

    boolean existsByEmpIdOrEmail(String empId, String email);

    boolean existsByEmail(String email);
}
