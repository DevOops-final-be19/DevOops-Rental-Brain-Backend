package com.devoops.rentalbrain.security;

import com.devoops.rentalbrain.employee.command.dto.UserImpl;
import com.devoops.rentalbrain.employee.command.service.EmployeeCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final EmployeeCommandService employeeCommandService;
    private final PasswordEncoder passwordEncoder;

    public JwtAuthenticationProvider(EmployeeCommandService employeeCommandService,
                                     PasswordEncoder passwordEncoder) {
        this.employeeCommandService = employeeCommandService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String empId = authentication.getName();
        String pwd = authentication.getCredentials().toString();

        // DB 로부터 사용자 정보 조회(UserDetails 객체로 반환)
        UserDetails userDetails = employeeCommandService.loadUserByUsername(empId);
        // BCrypt 암호 매칭
        if(!passwordEncoder.matches(pwd, userDetails.getPassword())){
            UserImpl userImpl = (UserImpl) userDetails;
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다. ,"+userImpl.getId());
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
