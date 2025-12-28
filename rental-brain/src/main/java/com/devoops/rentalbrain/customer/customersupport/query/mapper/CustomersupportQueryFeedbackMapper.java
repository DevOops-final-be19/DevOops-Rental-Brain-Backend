package com.devoops.rentalbrain.customer.customersupport.query.mapper;

import com.devoops.rentalbrain.customer.customersupport.query.dto.FeedbackDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.FeedbackSearchDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CustomersupportQueryFeedbackMapper {
    List<FeedbackDTO> selectFeedbackList(FeedbackSearchDTO searchDTO);
    int countFeedbackList(FeedbackSearchDTO searchDTO);
    FeedbackDTO selectFeedbackDetail(Long id);
    Map<String, Object> selectFeedbackKpi();
}