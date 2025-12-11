package com.devoops.rentalbrain.customer.customerlist.query.mapper;

import com.devoops.rentalbrain.customer.common.CustomerDto;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerlistSearchDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CustomerlistQueryMapper {
    // 검색 조건(DTO)을 받아 목록 조회
    List<CustomerDto> selectCustomerList(CustomerlistSearchDTO searchDto);

    // 전체 개수 조회
    long selectCustomerCount(CustomerlistSearchDTO searchDto);

    // 상세 조회
    CustomerDto selectCustomerById(@Param("id") Long id);
}