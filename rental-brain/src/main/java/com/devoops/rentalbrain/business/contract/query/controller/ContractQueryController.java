package com.devoops.rentalbrain.business.contract.query.controller;


import com.devoops.rentalbrain.business.contract.query.dto.*;
import com.devoops.rentalbrain.business.contract.query.service.ContractQueryService;
import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequestMapping("/contract")
@Tag(name = "계약조회(Query)", description = "계약 조회 API")
public class ContractQueryController {

    private final ContractQueryService contractQueryService;

    @Autowired
    public ContractQueryController(ContractQueryService contractQueryService) {
        this.contractQueryService = contractQueryService;
    }

    @Operation(
            summary = "계약 목록 조회",
            description = """
            계약 목록을 페이징하여 조회합니다.
            
            검색 조건
            - type : 검색 타입 (예: name, customer)
            - keyword : 검색어
            - status : 계약 상태 (P/E/I/C 등)
            
            페이징
            - page : 페이지 번호 (1부터 시작)
            - size : 페이지당 개수
            """
    )
    @ApiResponse(
            responseCode = "200",
            description = "계약 목록 조회 성공",
            content = @Content(
                    schema = @Schema(implementation = PageResponseDTO.class)
            )
    )
    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<AllContractDTO>> list(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        ContractSearchDTO criteria = new ContractSearchDTO(page, size);
        criteria.setType(type);
        criteria.setKeyword(keyword);
        criteria.setStatus(status);

        return ResponseEntity.ok(contractQueryService.getContractListWithPaging(criteria));
    }

    @Operation(
            summary = "계약 요약 정보 조회",
            description = """
            계약 현황 요약 정보를 조회합니다.
            
            예:
            - 전체 계약 수
            - 진행중 계약 수
            - 만료 임박 계약 수
            - 종료 계약 수
            """
    )
    @ApiResponse(
            responseCode = "200",
            description = "계약 요약 조회 성공",
            content = @Content(
                    schema = @Schema(implementation = ContractSummaryDTO.class)
            )
    )
    @GetMapping("/status")
    public ResponseEntity<ContractSummaryDTO> summary(){
        return ResponseEntity.ok(contractQueryService.getContractSummary());
    }

    @Operation(
            summary = "계약 기본 정보 조회",
            description = """
            계약 상세 화면 상단에 필요한 기본 정보를 한 번에 조회합니다.

            포함 정보
            - 계약 개요 정보
            - 계약 진행률
            - 수납 연체 건수
            - 렌탈 자산 개수
            """
    )
    @ApiResponse(
            responseCode = "200",
            description = "계약 기본 정보 조회 성공",
            content = @Content(
                    schema = @Schema(implementation = ContractBasicInfoDTO.class)
            )
    )
    @GetMapping("/{contractId}/basic-info")
    public ResponseEntity<ContractBasicInfoDTO> getContractBasicInfo(
            @PathVariable Long contractId
    ){
      return ResponseEntity.ok(
              contractQueryService.getContractBasicInfo(contractId)
      );
    }

    @Operation(
            summary = "계약 아이템 정보 조회",
            description = """
            계약에 포함된 아이템 정보를 조회합니다.

            포함 정보
            - 아이템 목록 요약 (아이템명 + 수량)
            - 아이템 상세 목록 (인스턴스 단위)
            """
    )
    @ApiResponse(
            responseCode = "200",
            description = "계약 아이템 정보 조회 성공",
            content = @Content(
                    schema = @Schema(implementation = ContractItemInfoDTO.class)
            )
    )
    @GetMapping("/{contractId}/items")
    public ResponseEntity<ContractItemInfoDTO> getContractItemInfo(
            @PathVariable Long contractId
    ){
        return ResponseEntity.ok(
                contractQueryService.getContractItemInfo(contractId)
        );
    }


    @Operation(
            summary = "계약 수납 정보 조회",
            description = "계약에 대한 월별 수납 정보를 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "계약 수납 조회 성공",
            content = @Content(
                    schema = @Schema(implementation = ContractPaymentDTO.class)
            )
    )
    @GetMapping("/{contractId}/payments")
    public ResponseEntity<List<ContractPaymentDTO>> getContractPayments(
            @PathVariable Long contractId
    ){
        return ResponseEntity.ok(
          contractQueryService.getContractPayments(contractId)
        );
    }


    @Operation(
            summary = "렌탈 제품 목록 조회",
            description = "계약에 포함된 렌탈 제품 정보를 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "렌탈 제품 조회 성공",
            content = @Content(
                    schema = @Schema(implementation = RentalProductInfoDTO.class)
            )
    )
    @GetMapping("/{contractId}/products")
    public ResponseEntity<List<RentalProductInfoDTO>> getRentalProductList(
            @PathVariable Long contractId
    ){
        return ResponseEntity.ok(
                contractQueryService.getRentalProductList(contractId)
        );
    }


    @Operation(summary = "계약 대상 고객 조회", description = "계약 생성을 위한 고객을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 고객"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/customer")
    public ResponseEntity<PageResponseDTO<CustomerContractApprovalDTO>> list(
            @ModelAttribute Criteria criteria
            ){
        return ResponseEntity.ok(contractQueryService.getCustomerContractApprovalList(criteria));
    }


    @Operation(
            summary = "계약 담당 직원 목록 조회",
            description = "계약 생성 시 선택 가능한 직원 목록을 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "직원 목록 조회 성공",
            content = @Content(
                    schema = @Schema(implementation = EmpContractDTO.class)
            )
    )
    @GetMapping("/emp")
    public ResponseEntity<List<EmpContractDTO>> getContractEmpList(){
        return ResponseEntity.ok(contractQueryService.getContractEmpList());
    }
}
