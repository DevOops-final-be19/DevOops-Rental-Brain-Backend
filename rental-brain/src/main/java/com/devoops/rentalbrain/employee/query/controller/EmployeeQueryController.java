package com.devoops.rentalbrain.employee.query.controller;

import com.devoops.rentalbrain.employee.query.dto.EmpAuthListDTO;
import com.devoops.rentalbrain.employee.query.dto.EmployeeInfoDTO;
import com.devoops.rentalbrain.employee.query.dto.InChargeDTO;
import com.devoops.rentalbrain.employee.query.dto.PositionDTO;
import com.devoops.rentalbrain.employee.query.service.EmployeeQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emp")
@Slf4j
@Tag(name = "직원 관리(Query)", description = "직원 정보 조회 관련 API")
public class EmployeeQueryController {
    private final EmployeeQueryService employeeQueryService;

    public EmployeeQueryController(EmployeeQueryService employeeQueryService) {
        this.employeeQueryService = employeeQueryService;
    }

    @Operation(
            summary = "내 정보 조회 (마이페이지)",
            description = "로그인한 직원 본인의 상세 정보를 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패")
            }
    )
    @GetMapping("/mypage")
    public ResponseEntity<?> getEmpInfoPage() {
        try {
            EmployeeInfoDTO employeeInfoDTO = employeeQueryService.getEmpInfoPage();
            return ResponseEntity.ok().body(employeeInfoDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 접근입니다.");
        }
    }

    @Operation(
            summary = "직원 목록 조회 (관리자)",
            description = "관리자가 전체 직원 목록을 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            }
    )
    @GetMapping("/admin/emplist")
    public ResponseEntity<List<EmployeeInfoDTO>> getEmpList() {
        try {
            List<EmployeeInfoDTO> employeeInfoDTO = employeeQueryService.getEmpList();
            return ResponseEntity.ok().body(employeeInfoDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/admin/empauthlist")
    public ResponseEntity<List<EmpAuthListDTO>> getEmpAuthList() {
        try {
            List<EmpAuthListDTO> empAuthListDTO = employeeQueryService.getEmpAuthList();
            return ResponseEntity.ok().body(empAuthListDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/admin/positionlist")
    public ResponseEntity<List<PositionDTO>> getPositionList() {
        try {
            List<PositionDTO> positionDTO = employeeQueryService.getPositionList();
            return ResponseEntity.ok().body(positionDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/incharge/list") // 직원 목록(admin X)
    public ResponseEntity<List<InChargeDTO>> getInChargeList() {
        try {
            List<InChargeDTO> inChargeDTO = employeeQueryService.getInChargeList();
            return ResponseEntity.ok().body(inChargeDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
