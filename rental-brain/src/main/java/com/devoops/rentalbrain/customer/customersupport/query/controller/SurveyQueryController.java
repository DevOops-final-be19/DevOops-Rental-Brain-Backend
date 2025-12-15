package com.devoops.rentalbrain.customer.customersupport.query.controller;

import com.devoops.rentalbrain.customer.common.SurveyCategoryDTO;
import com.devoops.rentalbrain.customer.common.SurveyDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.SurveyAndCategoryDTO;
import com.devoops.rentalbrain.customer.customersupport.query.dto.SurveyResultDTO;
import com.devoops.rentalbrain.customer.customersupport.query.service.SurveyQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/survey")
public class SurveyQueryController {
    private final SurveyQueryService surveyQueryService;
    public SurveyQueryController(SurveyQueryService surveyQueryService) {
        this.surveyQueryService = surveyQueryService;
    }

    @GetMapping("/category")
    public List<SurveyCategoryDTO> getSurveyCategory(){
        return surveyQueryService.getSurveyCategory();
    }

    @GetMapping("/list")
    public List<SurveyAndCategoryDTO> getAllSurveyList(){
        return surveyQueryService.getAllSurveyList();
    }

    @GetMapping("/list/{id}")
    public SurveyResultDTO getSurveyResult(@PathVariable Long id){
        return surveyQueryService.getSurveyResult(id);
    }
}
