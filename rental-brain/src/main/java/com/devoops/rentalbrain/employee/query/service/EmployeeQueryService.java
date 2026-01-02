package com.devoops.rentalbrain.employee.query.service;

import com.devoops.rentalbrain.employee.query.dto.ContractEmpDTO;
import com.devoops.rentalbrain.employee.query.dto.EmpAuthListDTO;
import com.devoops.rentalbrain.employee.query.dto.EmployeeInfoDTO;
import com.devoops.rentalbrain.employee.query.dto.InChargeDTO;
import com.devoops.rentalbrain.employee.query.dto.PositionDTO;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface EmployeeQueryService {
    List<GrantedAuthority> getUserAuth(Long empId);

    EmployeeInfoDTO getEmpInfoPage();

    List<EmployeeInfoDTO> getEmpList();

    List<EmpAuthListDTO> getEmpAuthList();

    List<InChargeDTO> getInChargeList();


    List<PositionDTO> getPositionList();
}
