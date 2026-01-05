package com.devoops.rentalbrain.customer.customersupport.command.service;

import com.devoops.rentalbrain.customer.common.SurveyDTO;
import com.devoops.rentalbrain.customer.customersupport.command.dto.SurveyDeleteDTO;
import com.devoops.rentalbrain.customer.customersupport.command.dto.SurveyModifyDTO;
import com.devoops.rentalbrain.customer.customersupport.command.entity.Survey;
import com.openai.models.responses.Response;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SurveyCommandService {
    Response csvAnalysis(MultipartFile csvFile) throws IOException;

    Survey startSurvey(SurveyDTO surveyDTO);

    void updateSurvey(SurveyModifyDTO surveyModifyDTO);

    void deleteSurvey(SurveyDeleteDTO surveyDeleteDTO);
}
