package com.devoops.rentalbrain.business.contract.query.controller;


import com.devoops.rentalbrain.business.contract.query.dto.AllContractDTO;
import com.devoops.rentalbrain.business.contract.query.dto.ContractSearchDTO;
import com.devoops.rentalbrain.business.contract.query.dto.ContractSummaryDTO;
import com.devoops.rentalbrain.business.contract.query.service.ContractQueryService;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequestMapping("/contract")
public class ContractQueryController {

    private final ContractQueryService contractQueryService;

    @Autowired
    public ContractQueryController(ContractQueryService contractQueryService) {
        this.contractQueryService = contractQueryService;
    }

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

    @GetMapping("/status")
    public ResponseEntity<ContractSummaryDTO> summary(){
        return ResponseEntity.ok(contractQueryService.getContractSummary());
    }
}
