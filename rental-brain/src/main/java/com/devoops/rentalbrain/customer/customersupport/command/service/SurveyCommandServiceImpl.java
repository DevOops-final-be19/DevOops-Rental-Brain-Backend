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
                " \"generatedAt\": string,\n" +
                " \"summary\": {\n" +
                " \"title\": string,\n" +
                " \"bullets\": string[]\n" +
                " },\n" +
                " \"charts\": [\n" +
                " {\n" +
                " \"id\": string,\n" +
                " \"title\": string,\n" +
                " \"type\": \"bar\" | \"doughnut\" | \"pie\" | \"line\",\n" +
                " \"data\": {\n" +
                " \"labels\": string[],\n" +
                " \"datasets\": [\n" +
                " {\n" +
                " \"label\": string,\n" +
                " \"values\": number[]\n" +
                " }\n" +
                " ]\n" +
                " },\n" +
                " \"options\": {\n" +
                " \"indexAxis\"?: \"x\" | \"y\",\n" +
                " \"unit\"?: string,\n" +
                " \"description\"?: string\n" +
                " }\n" +
                " }\n" +
                " ],\n" +
                " \"recommendations\": [\n" +
                "  {\n" +
                "   \"name\": string,\n" +
                "   \"content\": string,\n" +
                "   \"segmentName\": \"잠재 고객\" | \"신규 고객\" | \"일반 고객\" | \"VIP 고객\" | \"확장 의사 고객 (기회 고객)\" | \"이탈 위험 고객\",\n" +
                "   \"rate\"?: number\n" +
                "  }\n" +
                " ]\n" +
                "}\n\n" +

                "고객 세그먼트 기준:\n" +
                "- 잠재 고객: 아직 계약 이력이 없는 고객으로, 문의·견적·상담 등 초기 접점만 존재하는 상태\n" +
                "- 신규 고객: 첫 계약을 체결한 직후의 고객으로, 아직 관계 안정성이 검증되지 않은 초기 거래 단계\n" +
                "- 일반 고객: 첫 계약 시작일 기준 3개월 이상 경과했고, 해지·연체·이탈 위험·확장 시그널이 없는 정상 거래 고객\n" +
                "- VIP 고객: 해지 이력이 없고, 계약 유지 개월 수 합계가 36개월 이상이거나 총 계약 금액이 3억 원 이상인 핵심 고객\n" +
                "- 확장 의사 고객 (기회 고객): 업셀링이 증가했거나 계약 만료가 3~6개월 이내이면서 만족도 4.0 이상인 고객 중, 해지 요청이나 연체가 없는 경우\n" +
                "- 이탈 위험 고객: 계약 만료가 1~3개월 이내이거나, 해지·연체가 발생했거나, 최근 3개월 평균 만족도가 2.5 이하이거나, 계약 종료 후 3개월 이내에 활성 계약이 없는 고객\n\n" +

                "추천 규칙:\n" +
                "- 프로모션(rate 없음): 금액 할인 제외! 장비 업그레이드, 경품 추첨, 정기점검 서비스, 추가 장비 체험 등\n" +
                "- 쿠폰(rate 있음): 금액 할인만! 할인율(%)을 rate에 숫자로 입력\n" +
                "- 각 추천마다 정확한 segmentName 필수 (위 6개 중 하나)\n" +
                "- 사무용품 렌탈 업체 특성 고려\n\n" +

                "STRICT RULES:\n" +
                "- Output ONLY JSON\n" +
                "- No markdown\n" +
                "- No explanations\n" +
                "- No HTML or JavaScript\n" +
                "- rate가 있으면 쿠폰, 없으면 프로모션\n\n" +

                "Below is the survey result data in CSV format.\n" +
                "Analyze this data and generate chart-ready JSON.\n\n" +

                "CSV DATA:\n" +
                new String(csvFile.getBytes(), StandardCharsets.UTF_8) +
                "\n" +
                "Return ONLY the JSON object.\n";


        ResponseCreateParams createParams = ResponseCreateParams.builder()
                .temperature(0.0)
                .topP(1.0)
                .input(
                        prompt
                )
                .model(ChatModel.GPT_5_1)
                .build();
        log.info("request : {}", createParams);

        Response response = client.responses().create(createParams);
        log.info("response : {}", response);

        return response;
    }

    @Override
    @Transactional
    public Survey startSurvey(SurveyDTO surveyDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Survey survey = modelMapper.map(surveyDTO, Survey.class);

        survey.setSurveyCode(codeGenerator.generate(CodeType.SURVEY));

        log.info("survey : {}", survey);
        Survey resultSurvey = surveyCommandRepository.save(survey);
        return resultSurvey;
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
