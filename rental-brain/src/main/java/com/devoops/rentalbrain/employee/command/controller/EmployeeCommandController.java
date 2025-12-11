package com.devoops.rentalbrain.employee.command.controller;


import com.devoops.rentalbrain.employee.command.dto.*;
import com.devoops.rentalbrain.employee.command.entity.EmployeeAuth;
import com.devoops.rentalbrain.employee.command.service.EmployeeCommandService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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
        try{
        employeeCommandService.signup(signUpDTO);
        }catch (Exception e){
            log.info("");
            return ResponseEntity.ok().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutDTO logoutDTO, HttpServletRequest request){
        employeeCommandService.logout(logoutDTO,request.getHeader("Authorization"));
        return ResponseEntity.ok().body("로그아웃 완료");
    }

    @PutMapping("/admin/auth/modify")
    public ResponseEntity<?> modifyAuth(@RequestBody List<EmployeeAuthDTO> employeeAuthDTO){
        employeeCommandService.modifyAuth(employeeAuthDTO);
        return ResponseEntity.ok().body("Done");
    }

    @PutMapping("/modify")
    public ResponseEntity<?> modifyEmpInfo(@RequestBody EmployeeInfoModifyDTO employeeInfoModifyDTO){
        try {
            employeeCommandService.modifyEmpInfo(employeeInfoModifyDTO);
        }catch (Exception e){
            return ResponseEntity.ok().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Done");
    }

    @PutMapping("/admin/info/modify")
    public ResponseEntity<?> modifyEmpInfoByAdmin(@RequestBody EmployeeInfoModifyByAdminDTO employeeInfoModifyByAdminDTO){
        try{
            employeeCommandService.modifyEmpInfoByAdmin(employeeInfoModifyByAdminDTO);
        } catch (Exception e){
            return ResponseEntity.ok().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Done");
    }

    @PutMapping("/pwdmodify")
    public ResponseEntity<?> modifyEmpPwd(@RequestBody EmployeePasswordModifyDTO employeePasswordModifyDTO){
        try {
            employeeCommandService.modifyEmpPwd(employeePasswordModifyDTO);
        }catch (Exception e){
            return ResponseEntity.ok().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Done");
    }
}
