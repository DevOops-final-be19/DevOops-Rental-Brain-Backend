package com.devoops.rentalbrain.customer.customersupport.query.service;

import com.devoops.rentalbrain.customer.common.SurveyCategoryDTO;
import com.devoops.rentalbrain.customer.common.SurveyDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.SurveyAndCategoryDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.SurveyResultDTO;

import java.util.List;

public interface SurveyQueryService {
    List<SurveyAndCategoryDTO> getAllSurveyList();

    List<SurveyCategoryDTO> getSurveyCategory();

    SurveyResultDTO getSurveyResult(Long id);
}
