package com.devoops.rentalbrain.customer.customersupport.command.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.files.FileCreateParams;
import com.openai.models.files.FileObject;
import com.openai.models.files.FilePurpose;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.ResponseInputFile;
import com.openai.models.responses.ResponseInputItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
public class SurveyCommandServiceImpl implements SurveyCommandService {

    public SurveyCommandServiceImpl() {

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
}
