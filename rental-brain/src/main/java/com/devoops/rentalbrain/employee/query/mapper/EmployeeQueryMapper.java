package com.devoops.rentalbrain.employee.query.mapper;

import com.devoops.rentalbrain.employee.query.dto.ContractEmpDTO;
import com.devoops.rentalbrain.employee.query.dto.EmpAuthListDTO;
import com.devoops.rentalbrain.employee.query.dto.EmployeeInfoDTO;
import com.devoops.rentalbrain.employee.query.dto.InChargeDTO;
import com.devoops.rentalbrain.employee.query.dto.PositionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Mapper
public interface EmployeeQueryMapper {
    List<String> getUserAuth(Long empId);

    EmployeeInfoDTO getEmpInfoPage(String empId);

    List<EmployeeInfoDTO> getEmpList();

    List<EmpAuthListDTO> getEmpAuthList();

    List<InChargeDTO> getInChargeList();


    List<PositionDTO> getPositionList();
}
