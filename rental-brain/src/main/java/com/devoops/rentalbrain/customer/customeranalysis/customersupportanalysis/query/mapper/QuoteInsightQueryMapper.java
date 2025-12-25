package com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.mapper;

import com.devoops.rentalbrain.customer.customeranalysis.customersupportanalysis.query.dto.QuoteInsightRowDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface QuoteInsightQueryMapper {

    List<QuoteInsightRowDTO> selectQuoteInsightRows(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("windowDays") int windowDays
            );

}
