package com.devoops.rentalbrain.customer.customersupport.query.mapper;

import com.devoops.rentalbrain.customer.customersupport.query.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CustomersupportQueryFeedbackMapper {
    List<FeedbackDTO> selectFeedbackList(FeedbackSearchDTO searchDto);
    long selectFeedbackCount(FeedbackSearchDTO searchDto);
    FeedbackDTO selectFeedbackById(@Param("id") Long id);
}