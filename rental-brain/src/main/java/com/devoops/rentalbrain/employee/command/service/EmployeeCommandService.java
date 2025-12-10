package com.devoops.rentalbrain.employee.command.service;


import com.devoops.rentalbrain.employee.command.dto.LogoutDTO;
import com.devoops.rentalbrain.employee.command.dto.SignUpDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface EmployeeCommandService extends UserDetailsService {
    void signup(SignUpDTO signUpDTO);

    void logout(LogoutDTO logoutDTO,String token);
}
