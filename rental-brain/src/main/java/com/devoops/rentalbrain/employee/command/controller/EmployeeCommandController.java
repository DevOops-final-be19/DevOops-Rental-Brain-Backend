package com.devoops.rentalbrain.employee.command.controller;


import com.devoops.rentalbrain.employee.command.dto.EmpPositionAuthDTO;
import com.devoops.rentalbrain.employee.command.dto.LogoutDTO;
import com.devoops.rentalbrain.employee.command.dto.SignUpDTO;
import com.devoops.rentalbrain.employee.command.service.EmployeeCommandService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emp")
public class EmployeeCommandController {
    private final EmployeeCommandService employeeCommandService;

    public EmployeeCommandController(EmployeeCommandService employeeCommandService) {
        this.employeeCommandService = employeeCommandService;
    }

    @GetMapping("/health")
    public String health(){
        return "I'm OK";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpDTO signUpDTO){
        employeeCommandService.signup(signUpDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutDTO logoutDTO, HttpServletRequest request){
        employeeCommandService.logout(logoutDTO,request.getHeader("Authorization"));
        return ResponseEntity.ok().body("로그아웃 완료");
    }

    @PutMapping("/auth/modify")
    public ResponseEntity<?> modifyAuth(@RequestBody List<EmpPositionAuthDTO> empPositionAuthDTO){
        employeeCommandService.modifyAuth(empPositionAuthDTO);
        return ResponseEntity.ok().body("Done");
    }
}
