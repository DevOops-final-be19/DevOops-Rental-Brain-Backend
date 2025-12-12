package com.devoops.rentalbrain.customer.customersupport.command.service;

import com.devoops.rentalbrain.common.codegenerator.CodeGenerator;
import com.devoops.rentalbrain.common.codegenerator.CodeType;
import com.devoops.rentalbrain.customer.common.SurveyDTO;
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

        ResponseCreateParams createParams = ResponseCreateParams.builder()
                .input("너는 CSV 데이터를 분석하여 HTML 코드만 생성하는 도우미다. Chart.js를 사용해서 요약글 + 차트로 내용을 정리하고 마지막에 추천 프로모션도 작성해라. 다른 설명은 절대 출력하지 마라.\n"+
                        new String(csvFile.getBytes(), StandardCharsets.UTF_8))
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
}
