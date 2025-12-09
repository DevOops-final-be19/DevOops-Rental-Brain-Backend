package com.devoops.rentalbrain.customer.customerlist.query.mapper;

import com.devoops.rentalbrain.customer.common.CustomerDto;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CustomerMapper {
    // 검색 DTO 사용
    List<CustomerDto> selectCustomerList(CustomerSearchDto searchDto);
    int selectCustomerCount(CustomerSearchDto searchDto);
    CustomerDto selectCustomerById(@Param("id") Long id);
}