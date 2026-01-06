package com.devoops.rentalbrain.common.ai.query.service;

import com.devoops.rentalbrain.common.ai.command.service.AiCommandService;
import com.devoops.rentalbrain.common.ai.common.EmbeddingDTO;
import com.openai.client.OpenAIClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@Rollback
class AiQueryServiceImplTest {
    @Autowired
    private AiQueryService aiQueryService;

    @MockitoBean
    private OpenAIClient openAIClient;

    @MockitoBean
    private AiCommandService aiCommandService;

    @DisplayName("피드백 단어 임베딩 조회 테스트")
    @Test
    void getFeedBackTest(){
        Assertions.assertDoesNotThrow(() -> {
            List<EmbeddingDTO> feedBacks = aiQueryService.getFeedBacks();
            Assertions.assertNotNull(feedBacks);
        });
    }
}