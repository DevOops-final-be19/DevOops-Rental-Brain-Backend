package com.devoops.rentalbrain.employee.query.service;

import com.devoops.rentalbrain.employee.query.dto.EmployeeInfoDTO;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface EmployeeQueryService {
    List<GrantedAuthority> getUserAuth(Long empId, Long positionId);

    EmployeeInfoDTO getEmpInfoPage(String empId);
}
