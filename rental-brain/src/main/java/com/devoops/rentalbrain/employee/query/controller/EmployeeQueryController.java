package com.devoops.rentalbrain.employee.query.controller;

import com.devoops.rentalbrain.employee.query.dto.EmployeeInfoDTO;
import com.devoops.rentalbrain.employee.query.service.EmployeeQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emp")
@Slf4j
public class EmployeeQueryController {
    private final EmployeeQueryService employeeQueryService;

    public EmployeeQueryController(EmployeeQueryService employeeQueryService) {
        this.employeeQueryService = employeeQueryService;
    }

    @GetMapping("/mypage/{empId}")
    public ResponseEntity<EmployeeInfoDTO> getEmpInfoPage(@PathVariable String empId) {
        EmployeeInfoDTO employeeInfoDTO = employeeQueryService.getEmpInfoPage(empId);
        log.info(employeeInfoDTO.toString());
        return ResponseEntity.ok().body(employeeInfoDTO);
    }

    @GetMapping("/admin/emplist")
    public ResponseEntity<EmployeeInfoDTO> getEmpList(){
        EmployeeInfoDTO employeeInfoDTO = new EmployeeInfoDTO();
        return ResponseEntity.ok().body(employeeInfoDTO);
    }

    @GetMapping("/admin/emppage")
    public ResponseEntity<EmployeeInfoDTO> getEmpInfo(){
        EmployeeInfoDTO employeeInfoDTO = new EmployeeInfoDTO();
        return ResponseEntity.ok().body(employeeInfoDTO);
    }
}
