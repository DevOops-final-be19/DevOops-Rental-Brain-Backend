package com.devoops.rentalbrain.employee.command.repository;

import com.devoops.rentalbrain.employee.command.entity.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryCommandRepository extends JpaRepository<LoginHistory,Long> {
}
