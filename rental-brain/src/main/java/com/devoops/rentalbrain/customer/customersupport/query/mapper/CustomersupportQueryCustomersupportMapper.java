package com.devoops.rentalbrain.customer.customersupport.query.mapper;

import com.devoops.rentalbrain.customer.customersupport.query.dto.CustomersupportDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.CustomersupportKpiDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.CustomersupportSearchDTO;
import com.devoops.rentalbrain.employee.query.dto.InChargeDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CustomersupportQueryCustomersupportMapper {
    List<CustomersupportDTO> selectSupportList(CustomersupportSearchDTO criteria);
    long selectSupportCount(CustomersupportSearchDTO criteria);
    CustomersupportDTO selectSupportById(Long id);
    CustomersupportKpiDTO selectSupportKpi();
    // 담당자(회원) 목록 조회
    List<InChargeDTO> selectInChargeList();
}