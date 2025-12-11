package com.devoops.rentalbrain.employee.query.controller;

import com.devoops.rentalbrain.employee.query.dto.EmployeeInfoDTO;
import com.devoops.rentalbrain.employee.query.service.EmployeeQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emp")
@Slf4j
public class EmployeeQueryController {
    private final EmployeeQueryService employeeQueryService;

    public EmployeeQueryController(EmployeeQueryService employeeQueryService) {
        this.employeeQueryService = employeeQueryService;
    }

    @GetMapping("/mypage")
    public ResponseEntity<?> getEmpInfoPage() {
        EmployeeInfoDTO employeeInfoDTO = employeeQueryService.getEmpInfoPage();
//        log.info(employeeInfoDTO.toString());
        if(employeeInfoDTO == null){
            return ResponseEntity.ok().body("잘못된 접근입니다.");
        }
        return ResponseEntity.ok().body(employeeInfoDTO);
    }

    @GetMapping("/admin/emplist")
    public ResponseEntity<List<EmployeeInfoDTO>> getEmpList(){
        List<EmployeeInfoDTO> employeeInfoDTO = employeeQueryService.getEmpList();
        return ResponseEntity.ok().body(employeeInfoDTO);
    }

}
