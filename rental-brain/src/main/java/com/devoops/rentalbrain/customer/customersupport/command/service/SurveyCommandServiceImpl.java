package com.devoops.rentalbrain.customer.customersupport.command.service;

import com.devoops.rentalbrain.common.codegenerator.CodeGenerator;
import com.devoops.rentalbrain.common.codegenerator.CodeType;
import com.devoops.rentalbrain.customer.common.SurveyDTO;
import com.devoops.rentalbrain.customer.customersupport.command.dto.SurveyDeleteDTO;
import com.devoops.rentalbrain.customer.customersupport.command.dto.SurveyModifyDTO;
import com.devoops.rentalbrain.customer.customersupport.command.entity.Survey;
import com.devoops.rentalbrain.customer.customersupport.command.repository.SurveyCommandRepository;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Service
@Slf4j
public class SurveyCommandServiceImpl implements SurveyCommandService {
    private final ModelMapper modelMapper;
    private final SurveyCommandRepository surveyCommandRepository;
    private final CodeGenerator codeGenerator;

    public SurveyCommandServiceImpl(ModelMapper modelMapper,
                                    SurveyCommandRepository surveyCommandRepository,
                                    CodeGenerator codeGenerator) {
        this.modelMapper = modelMapper;
        this.surveyCommandRepository = surveyCommandRepository;
        this.codeGenerator = codeGenerator;
    }

    @Override
    public Response csvAnalysis(MultipartFile csvFile) throws IOException {
        long kb = 1024L;

        if(csvFile.getSize() >= 250 * kb){
            throw new IllegalArgumentException("파일 크기가 너무 큽니다.");
        }

        OpenAIClient client = OpenAIOkHttpClient.fromEnv();

        String prompt = "SYSTEM:\n" +
                "You are a data analysis engine.\n" +
                "You must output ONLY valid JSON.\n" +
                "Do not include explanations, markdown, HTML, or JavaScript.\n\n" +

                "USER:\n" +
                "You MUST return the result strictly following this JSON structure:\n" +
                "{\n" +
                "  \"generatedAt\": string,\n" +
                "  \"summary\": {\n" +
                "    \"title\": string,\n" +
                "    \"bullets\": string[]\n" +
                "  },\n" +
                "  \"charts\": [\n" +
                "    {\n" +
                "      \"id\": string,\n" +
                "      \"title\": string,\n" +
                "      \"type\": \"bar\" | \"doughnut\" | \"pie\" | \"line\",\n" +
                "      \"data\": {\n" +
                "        \"labels\": string[],\n" +
                "        \"datasets\": [\n" +
                "          {\n" +
                "            \"label\": string,\n" +
                "            \"values\": number[]\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      \"options\": {\n" +
                "        \"indexAxis\"?: \"x\" | \"y\",\n" +
                "        \"unit\"?: string,\n" +
                "        \"description\"?: string\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"recommendations\": [\n" +
                "    {\n" +
                "      \"title\": string,\n" +
                "      \"description\": string,\n" +
                "      \"tags\": string[]\n" +
                "    }\n" +
                "  ]\n" +
                "}\n\n" +

                "All human-readable text values MUST be written in Korean.\n\n" +

                "STRICT RULES:\n" +
                "- Output ONLY JSON\n" +
                "- No markdown\n" +
                "- No explanations\n" +
                "- No HTML or JavaScript\n\n" +

                "Below is the survey result data in CSV format.\n" +
                "Analyze this data and generate chart-ready JSON.\n\n" +

                "CSV DATA:\n" +
                new String(csvFile.getBytes(), StandardCharsets.UTF_8) +
                "\n" +
                "Return ONLY the JSON object.\n";
        log.info(prompt);

        ResponseCreateParams createParams = ResponseCreateParams.builder()
                .temperature(0.0)
                .topP(1.0)
                .input(
                        prompt
                )
                .model(ChatModel.GPT_5_1)
                .build();
        log.info("response 값 : {}", createParams);

        Response response = client.responses().create(createParams);
        log.info("response 값 : {}", response);

        return response;
    }

    @Override
    @Transactional
    public void startSurvey(SurveyDTO surveyDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Survey survey = modelMapper.map(surveyDTO, Survey.class);

        survey.setSurveyCode(codeGenerator.generate(CodeType.SURVEY));

        log.info("survey : {}", survey);
        surveyCommandRepository.save(survey);
    }

    @Override
    @Transactional
    public void updateSurvey(SurveyModifyDTO surveyModifyDTO) {
        try{
        Survey survey = surveyCommandRepository.findById(surveyModifyDTO.getId()).get();
        modifySurvey(surveyModifyDTO,survey);
        } catch(Exception e){
            throw new RuntimeException("설문 수정 실패"+e);
        }
    }

    @Override
    @Transactional
    public void deleteSurvey(SurveyDeleteDTO surveyDeleteDTO) {
        try{
            Survey survey = surveyCommandRepository.findById(surveyDeleteDTO.getId()).get();
            surveyCommandRepository.delete(survey);
        } catch (Exception e) {
            throw new RuntimeException("설문 삭제 실패"+e);
        }
    }

    private void modifySurvey(SurveyModifyDTO surveyModifyDTO, Survey survey) {
        if(!survey.getName().equals(surveyModifyDTO.getName())){
            survey.setName(surveyModifyDTO.getName());
        }
        if(!survey.getLink().equals(surveyModifyDTO.getLink())){
            survey.setLink(surveyModifyDTO.getLink());
        }
        if(!survey.getStatus().equals(surveyModifyDTO.getStatus())){
            survey.setStatus(surveyModifyDTO.getStatus());
        }
        if(!survey.getStartDate().equals(surveyModifyDTO.getStartDate())){
            survey.setStartDate(surveyModifyDTO.getStartDate());
        }
        if(!survey.getEndDate().equals(surveyModifyDTO.getEndDate())){
            survey.setEndDate(surveyModifyDTO.getEndDate());
        }
        if(!survey.getCategoryId().equals(surveyModifyDTO.getCategoryId())){
            survey.setCategoryId(surveyModifyDTO.getCategoryId());
        }
    }
}
