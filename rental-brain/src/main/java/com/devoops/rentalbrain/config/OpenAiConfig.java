package com.devoops.rentalbrain.config;

import com.openai.client.okhttp.OpenAIOkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.openai.client.OpenAIClient;


@Configuration
public class OpenAiConfig {

    @Bean
    public OpenAIClient openAIClient() {
        // OPENAI_API_KEY 환경변수 자동 사용
        return OpenAIOkHttpClient.fromEnv();
    }
}

