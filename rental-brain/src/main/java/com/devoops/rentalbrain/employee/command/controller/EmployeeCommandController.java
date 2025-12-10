package com.devoops.rentalbrain.employee.command.controller;


import com.devoops.rentalbrain.employee.command.dto.SignUpDTO;
import com.devoops.rentalbrain.employee.command.service.EmployeeCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    
}
