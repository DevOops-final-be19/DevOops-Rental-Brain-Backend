package com.devoops.rentalbrain.customer.customersupport.query.mapper;

import com.devoops.rentalbrain.customer.common.SurveyCategoryDTO;
import com.devoops.rentalbrain.customer.common.SurveyDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.SurveyAndCategoryDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.SurveyResultDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SurveyQueryMapper {

    List<SurveyCategoryDTO> getSurveyCategory();

    List<SurveyAndCategoryDTO> getAllSurveyList();

    SurveyResultDTO getSurveyResult(Long id);
}
