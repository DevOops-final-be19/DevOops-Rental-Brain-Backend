package com.devoops.rentalbrain.customer.customerlist.query.service;

import com.devoops.rentalbrain.common.pagination.Criteria;
import com.devoops.rentalbrain.common.pagination.PageResponseDTO; // 파일 경로 대소문자 확인 필요 (보통 패키지는 소문자)
import com.devoops.rentalbrain.customer.common.CustomerDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerContractDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerDetailResponseDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerKpiDTO; // KPI DTO Import
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerlistSearchDTO;

public interface CustomerlistQueryService {

    // 1. KPI 조회 (신규 추가)
    CustomerKpiDTO getCustomerKpi();

    // 2. 페이징 목록 조회 (기존 유지, CustomerDTO -> CustomerDto 확인 필요)
    PageResponseDTO<CustomerDTO> getCustomerListWithPaging(CustomerlistSearchDTO criteria);

    // 3. 상세 조회 (반환 타입 변경: CustomerDto -> CustomerDetailResponseDTO)
    CustomerDetailResponseDTO getCustomerDetail(Long id);
}