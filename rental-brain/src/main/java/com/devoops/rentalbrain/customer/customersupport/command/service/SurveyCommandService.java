package com.devoops.rentalbrain.customer.customersupport.command.service;

import com.openai.models.responses.Response;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SurveyCommandService {
    Response csvAnalysis(MultipartFile csvFile) throws IOException;
}
