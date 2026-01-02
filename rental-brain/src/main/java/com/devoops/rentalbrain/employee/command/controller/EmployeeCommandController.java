package com.devoops.rentalbrain.employee.command.controller;


import com.devoops.rentalbrain.employee.command.dto.*;
import com.devoops.rentalbrain.employee.command.entity.EmployeeAuth;
import com.devoops.rentalbrain.employee.command.service.EmployeeCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/emp")
@Tag(name = "직원 관리(Command)",
        description = "직원 등록, 수정, 권한 변경 등 직원 관련 명령(Command) API")
public class EmployeeCommandController {
    private final EmployeeCommandService employeeCommandService;

    public EmployeeCommandController(EmployeeCommandService employeeCommandService) {
        this.employeeCommandService = employeeCommandService;
    }

    @Operation(
            summary = "헬스 체크",
            description = "서버 상태 확인용 API (인증 불필요)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "서버 정상 동작")
            }
    )
    @GetMapping("/health")
    public String health() {
        return "I'm OK";
    }

    @Operation(
            summary = "직원 회원가입",
            description = "신규 직원을 등록합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원가입 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @PostMapping("/admin/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpDTO signUpDTO){
        try{
        employeeCommandService.signup(signUpDTO);
        }catch (Exception e){
            log.info("");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "로그아웃",
            description = "JWT 토큰을 무효화하여 로그아웃 처리합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그아웃 완료")
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutDTO logoutDTO, HttpServletRequest request) {
        employeeCommandService.logout(logoutDTO, request.getHeader("Authorization"));
        return ResponseEntity.ok().body("로그아웃 완료");
    }

    @Operation(
            summary = "직원 권한 수정 (관리자)",
            description = "관리자가 여러 직원의 권한을 일괄 수정합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "권한 수정 완료"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            }
    )
    @PutMapping("/admin/auth/modify")
    public ResponseEntity<?> modifyAuth(@RequestBody EmployeeAuthDTO employeeAuthDTO) {
        try {
            employeeCommandService.modifyAuth(employeeAuthDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok().body("Done");
    }

    @Operation(
            summary = "직원 정보 수정 (본인)",
            description = "로그인한 직원 본인의 정보를 수정합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "수정 완료")
            }
    )
    @PutMapping("/modify")
    public ResponseEntity<?> modifyEmpInfo(@RequestBody EmployeeInfoModifyDTO employeeInfoModifyDTO) {
        try {
            employeeCommandService.modifyEmpInfo(employeeInfoModifyDTO);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok().body("Done");
    }

    @Operation(
            summary = "직원 정보 수정 (관리자)",
            description = "관리자가 직원의 정보를 수정합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "수정 완료"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            }
    )
    @PutMapping("/admin/info/modify")
    public ResponseEntity<?> modifyEmpInfoByAdmin(@RequestBody EmployeeInfoModifyByAdminDTO employeeInfoModifyByAdminDTO) {
        try {
            employeeCommandService.modifyEmpInfoByAdmin(employeeInfoModifyByAdminDTO);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok().body("Done");
    }

    @Operation(
            summary = "비밀번호 변경",
            description = "직원이 본인의 비밀번호를 변경합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "비밀번호 변경 완료")
            }
    )
    @PutMapping("/pwdmodify")
    public ResponseEntity<?> modifyEmpPwd(@RequestBody EmployeePasswordModifyDTO employeePasswordModifyDTO) {
        try {
            employeeCommandService.modifyEmpPwd(employeePasswordModifyDTO);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok().body("Done");
    }
}
