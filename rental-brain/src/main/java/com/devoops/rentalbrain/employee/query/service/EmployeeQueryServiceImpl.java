package com.devoops.rentalbrain.employee.query.service;

import com.devoops.rentalbrain.employee.command.dto.UserImpl;
import com.devoops.rentalbrain.employee.query.dto.ContractEmpDTO;
import com.devoops.rentalbrain.employee.query.dto.EmpAuthListDTO;
import com.devoops.rentalbrain.employee.query.dto.EmployeeInfoDTO;
import com.devoops.rentalbrain.employee.query.dto.InChargeDTO;
import com.devoops.rentalbrain.employee.query.dto.PositionDTO;
import com.devoops.rentalbrain.employee.query.mapper.EmployeeQueryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeQueryServiceImpl implements EmployeeQueryService {
    private final EmployeeQueryMapper employeeQueryMapper;

    public EmployeeQueryServiceImpl(EmployeeQueryMapper employeeQueryMapper) {
        this.employeeQueryMapper = employeeQueryMapper;
    }

    public List<GrantedAuthority> getUserAuth(Long empId){
        return employeeQueryMapper.getUserAuth(empId).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public EmployeeInfoDTO getEmpInfoPage() {
        UserImpl user = (UserImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String accessInfo = user.getEmpId();
        return employeeQueryMapper.getEmpInfoPage(accessInfo);
    }

    @Override
    public List<EmployeeInfoDTO> getEmpList() {
        return employeeQueryMapper.getEmpList();
    }

    @Override
    public List<EmpAuthListDTO> getEmpAuthList() {
        return employeeQueryMapper.getEmpAuthList();
    }

    @Override
    public List<InChargeDTO> getInChargeList() {
        return employeeQueryMapper.getInChargeList();
    }

    @Override
    public List<PositionDTO> getPositionList() {
        return employeeQueryMapper.getPositionList();
    }
}
