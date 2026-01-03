package com.devoops.rentalbrain.customer.customersupport.command.controller;

import com.devoops.rentalbrain.customer.common.SurveyDTO;
import com.devoops.rentalbrain.customer.customersupport.command.dto.SurveyDeleteDTO;
import com.devoops.rentalbrain.customer.customersupport.command.dto.SurveyModifyDTO;
import com.devoops.rentalbrain.customer.customersupport.command.entity.Survey;
import com.devoops.rentalbrain.customer.customersupport.command.service.SurveyCommandService;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseOutputText;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/survey")
@Slf4j
public class SurveyController {
    private final SurveyCommandService surveyCommandService;

    public SurveyController(SurveyCommandService surveyCommandService) {
        this.surveyCommandService = surveyCommandService;
    }

    @PostMapping("/csv/analysis")
    public ResponseEntity<?> csvAnalysis(@RequestParam("file") MultipartFile csvFile) throws IOException {
        log.info("csvFile 내용={}", csvFile);
        Response response = surveyCommandService.csvAnalysis(csvFile);

        String result = response.output().stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .map(ResponseOutputText::text)
                .filter(s->!s.equals("```html")&&!s.equals("```"))
                .reduce("",(a,b)->a+b);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startSurvey(@RequestBody SurveyDTO surveyDTO) {
        Survey survey = surveyCommandService.startSurvey(surveyDTO);
        return ResponseEntity.ok(Map.of(
                "surveyId", survey.getId(),
                "message", "설문 생성 성공"
        ));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateSurvey(@RequestBody SurveyModifyDTO surveyModifyDTO) {
        surveyCommandService.updateSurvey(surveyModifyDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteSurvey(@RequestBody SurveyDeleteDTO surveyDeleteDTO) {
        surveyCommandService.deleteSurvey(surveyDeleteDTO);
        return ResponseEntity.ok().build();
    }
}
