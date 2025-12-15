package com.devoops.rentalbrain.customer.customersupport.query.mapper;

import com.devoops.rentalbrain.customer.customersupport.query.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CustomersupportQueryCustomersupportMapper {
    List<CustomersupportDTO> selectSupportList(CustomersupportSearchDTO searchDto);
    long selectSupportCount(CustomersupportSearchDTO searchDto);
    CustomersupportDTO selectSupportById(@Param("id") Long id);
}