package com.devoops.rentalbrain.customer.customerlist.query.mapper;

import com.devoops.rentalbrain.customer.common.CustomerDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerDetailResponseDTO; // Inner DTO 사용을 위해 import
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerlistSearchDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.CustomersupportDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.FeedbackDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerlistQueryMapper {

    // 목록 조회
    List<CustomerDTO> selectCustomerList(CustomerlistSearchDTO criteria);
    long selectCustomerCount(CustomerlistSearchDTO criteria);

    // 기본 상세 조회 (CustomerDto 반환) -> Service에서 호출 후 DetailDTO로 변환
    CustomerDTO selectCustomerById(@Param("id") Long id);

    // 상세 내역 조회용 메서드들
    List<CustomersupportDTO> selectSupportsByCustomerId(@Param("customerId") Long customerId);
    List<FeedbackDTO> selectFeedbacksByCustomerId(@Param("customerId") Long customerId);
    List<CustomerDetailResponseDTO.CustomerQuoteDTO> selectQuotesByCustomerId(@Param("customerId") Long customerId);
    List<CustomerDetailResponseDTO.CustomerContractDTO> selectContractsByCustomerId(@Param("customerId") Long customerId);
    List<CustomerDetailResponseDTO.CustomerAsDTO> selectAsByCustomerId(@Param("customerId") Long customerId);
    List<CustomerDetailResponseDTO.CustomerCouponDTO> selectCouponsByCustomerId(@Param("customerId") Long customerId);
    List<CustomerDetailResponseDTO.CustomerPromotionDTO> selectPromotionsByCustomerId(@Param("customerId") Long customerId);
}