package com.devoops.rentalbrain.customer.customersupport.query.service;

import com.devoops.rentalbrain.customer.common.SurveyCategoryDTO;
import com.devoops.rentalbrain.customer.common.SurveyDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.SurveyAndCategoryDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.SurveyResultDTO;
import com.devoops.rentalbrain.customer.customersupport.query.mapper.SurveyQueryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SurveyQueryServiceImpl implements SurveyQueryService {
    private final SurveyQueryMapper surveyQueryMapper;

    public SurveyQueryServiceImpl(SurveyQueryMapper surveyQueryMapper) {
        this.surveyQueryMapper = surveyQueryMapper;
    }

    public List<SurveyCategoryDTO> getSurveyCategory(){
        return surveyQueryMapper.getSurveyCategory();
    }

    @Override
    public List<SurveyAndCategoryDTO> getAllSurveyList() {
        return surveyQueryMapper.getAllSurveyList();
    }

    @Override
    public SurveyResultDTO getSurveyResult(Long id) {
        return surveyQueryMapper.getSurveyResult(id);
    }
}
