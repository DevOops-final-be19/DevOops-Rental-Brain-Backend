package com.devoops.rentalbrain.employee.query.service;

import com.devoops.rentalbrain.employee.query.dto.EmployeeInfoDTO;
import com.devoops.rentalbrain.employee.query.mapper.EmployeeQueryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

    public List<GrantedAuthority> getUserAuth(Long empId, Long positionId){
        return employeeQueryMapper.getUserAuth(empId,positionId).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

    }

    public EmployeeInfoDTO getEmpInfoPage(String empId) {
        log.info("{}",empId);
        return employeeQueryMapper.getEmpInfoPage(empId);
    }
}
