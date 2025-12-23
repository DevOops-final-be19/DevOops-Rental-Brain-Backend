package com.devoops.rentalbrain.employee.command.service;

import com.devoops.rentalbrain.employee.command.dto.*;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface EmployeeCommandService extends UserDetailsService {
    void signup(SignUpDTO signUpDTO);

    void logout(LogoutDTO logoutDTO,String token);

    void modifyAuth(EmployeeAuthDTO empPositionAuthDTO);

    void saveLoginHistory(Long id, String ipAddress, char y);

    void modifyEmpInfo(EmployeeInfoModifyDTO employeeInfoModifyDTO);

    void modifyEmpInfoByAdmin(EmployeeInfoModifyByAdminDTO employeeInfoModifyByAdminDTO);

    void modifyEmpPwd(EmployeePasswordModifyDTO employeePasswordModifyDTO);
}
