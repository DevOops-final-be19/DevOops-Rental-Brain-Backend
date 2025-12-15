package com.devoops.rentalbrain.business.contract.command.controller;


import com.devoops.rentalbrain.business.contract.command.dto.ContractCreateDTO;
import com.devoops.rentalbrain.business.contract.command.service.ContractCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
@Slf4j
public class ContractCommandController {

    private final ContractCommandService contractCommandService;

    @PostMapping
    public ResponseEntity<Void> createContract(
            @RequestBody ContractCreateDTO dto
            ) {
        log.info("[계약 생성 요청] {}", dto);

        contractCommandService.createContract(dto);
        return ResponseEntity.ok().build();
    }
}
