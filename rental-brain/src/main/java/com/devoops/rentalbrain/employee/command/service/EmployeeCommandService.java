package com.devoops.rentalbrain.employee.command.service;


import com.devoops.rentalbrain.employee.command.dto.EmpPositionAuthDTO;
import com.devoops.rentalbrain.employee.command.dto.LogoutDTO;
import com.devoops.rentalbrain.employee.command.dto.SignUpDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface EmployeeCommandService extends UserDetailsService {
    void signup(SignUpDTO signUpDTO);

    void logout(LogoutDTO logoutDTO,String token);

    void modifyAuth(List<EmpPositionAuthDTO> empPositionAuthDTO);

    void saveLoginHistory(Long id, String ipAddress, char y);
}
