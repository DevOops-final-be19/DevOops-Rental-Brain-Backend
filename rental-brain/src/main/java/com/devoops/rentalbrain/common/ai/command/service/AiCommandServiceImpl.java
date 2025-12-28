package com.devoops.rentalbrain.common.ai.command.service;

import com.devoops.rentalbrain.common.ai.command.dto.KeywordCountDTO;
import com.devoops.rentalbrain.common.ai.command.dto.MetaDataDTO;
import com.devoops.rentalbrain.common.ai.common.EmbeddingDTO;
import com.devoops.rentalbrain.common.ai.command.repository.OpenSearchVectorRepository;
import com.devoops.rentalbrain.common.ai.common.SentimentDTO;
import com.devoops.rentalbrain.common.ai.query.service.AiQueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.embeddings.CreateEmbeddingResponse;
import com.openai.models.embeddings.EmbeddingCreateParams;
import com.openai.models.embeddings.EmbeddingModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseOutputItem;
import com.openai.models.responses.ResponseOutputText;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AiCommandServiceImpl implements AiCommandService {
    private final OpenAIClient openAIClient;
    private final OpenSearchVectorRepository openSearchVectorRepository;
    private final AiQueryService aiQueryService;
    private final PromptCommandService promptCommandService;

    public AiCommandServiceImpl(OpenAIClient openAIClient,
                                OpenSearchVectorRepository openSearchVectorRepository,
                                AiQueryService aiQueryService,
                                PromptCommandService promptCommandService) {
        this.openAIClient = openAIClient;
        this.openSearchVectorRepository = openSearchVectorRepository;
        this.aiQueryService = aiQueryService;
        this.promptCommandService = promptCommandService;
    }

    public List<Float> embed(String input) {
        EmbeddingCreateParams params = EmbeddingCreateParams.builder()
                .model(EmbeddingModel.TEXT_EMBEDDING_3_SMALL) // 1536-d :contentReference[oaicite:7]{index=7}
                .input(input)
                .build();

        CreateEmbeddingResponse res = openAIClient.embeddings().create(params);
        log.info("Embedding created: {}", res);
        // 첫 번째 벡터만 사용(단일 input)
        return res.data().get(0).embedding();
    }

    @Transactional(readOnly = true)
    public void indexDocument() throws IOException {
        for (EmbeddingDTO embeddingDTO : aiQueryService.getFeedBacks()) {
            String prompt = promptCommandService.buildVocabSentimentPrompt(embeddingDTO.getText());

            Response response = openAIClient.responses().create(
                    ResponseCreateParams.builder()
                            .model(ChatModel.GPT_5_1)
                            .input(prompt)
                            .temperature(0)
                            .build()
            );

            String outputText = response.output().stream()
                    .flatMap(item -> item.message().stream())
                    .flatMap(message -> message.content().stream())
                    .flatMap(content -> content.outputText().stream())
                    .map(ResponseOutputText::text)
                    .reduce("", (a, b) -> a + b);

            String json = outputText.trim();
            if (!json.startsWith("{")) {
                throw new IllegalStateException("LLM JSON malformed: " + json);
            }

            ObjectMapper mapper = new ObjectMapper();

            SentimentDTO sentimentDTO = mapper.readValue(outputText, SentimentDTO.class);

            List<Float> vector = embed(embeddingDTO.getText());

            Map<String, Object> doc = new HashMap<>();
            doc.put("chunkId", embeddingDTO.getChunkId());
            doc.put("text", embeddingDTO.getText());
            doc.put("embedding", vector);
            doc.put("source", embeddingDTO.getSource());
            doc.put("sourceId", embeddingDTO.getSourceId());
            doc.put("customerId", embeddingDTO.getCustomerId());
            doc.put("segments", embeddingDTO.getSegments());
            doc.put("sentiment", sentimentDTO.getSentiment());
            doc.put("score", embeddingDTO.getScore());
            doc.put("category", embeddingDTO.getCategory());
            doc.put("priority", embeddingDTO.getPriority());
            doc.put("status", embeddingDTO.getStatus());
            doc.put("vocab", sentimentDTO.getVocab());
            doc.put("createAt", embeddingDTO.getCreateAt());
            log.info("indexOneDocument: {}", doc);

            openSearchVectorRepository.upsertChunk(embeddingDTO.getChunkId(), doc);
        }
    }

    //    public List<String> retrieveTopK(String question, int k) throws IOException {
//        List<Float> qVec = embed(question);
//        SearchResponse<Map> res = openSearchVectorRepository.knnSearch(qVec, k);
//
//        List<String> texts = new ArrayList<>();
//        res.hits().hits().forEach(hit -> {
//            Map src = hit.source();
//            if (src != null && src.get("text") != null) texts.add(String.valueOf(src.get("text")));
//        });
//        return texts;
//    }
    public List<String> retrieveTopK(String question, Map<String, Object> filter, int k) throws IOException {
        List<Float> qVec = embed(question);

        float[] arr = new float[qVec.size()];
        for (int i = 0; i < qVec.size(); i++) {
            arr[i] = qVec.get(i);
        }

        SearchResponse<Map> res =
                openSearchVectorRepository.knnSearchWithFilter(arr, k, filter);

        return res.hits().hits().stream()
                .map(Hit::source)
                .filter(src -> src != null && src.get("text") != null)
                .map(src -> String.valueOf(src.get("text")))
                .distinct()
                .toList();
    }


    public Response answer(String question) throws IOException {
        MetaDataDTO meta = extract(question);
        log.info("meta: {}", meta);
        Map<String, Object> filter = meta.toFilterMap();

        List<String> contexts = retrieveTopK(question,filter, 10);

        if (contexts.isEmpty()) {
            return openAIClient.responses().create(
                    ResponseCreateParams.builder()
                            .model(ChatModel.GPT_5_1)
                            .input("문서에서 근거를 찾지 못했습니다.")
                            .build()
            );
        }

        String input = promptCommandService.buildPrompt(contexts,question,meta.getResponseStyle());

        log.info("input: {}", input);


        ResponseCreateParams params = ResponseCreateParams.builder()
                .model(ChatModel.GPT_5_1)
                .input(input)
                .temperature(0.2)
                .build();

        return openAIClient.responses().create(params);
    }

    public List<KeywordCountDTO> getTop3NegativeKeywords() throws IOException {
        return openSearchVectorRepository.getTopKeywords("부정", 3);
    }

    public List<KeywordCountDTO> getTop3PositiveKeywords() throws IOException {
        return openSearchVectorRepository.getTopKeywords("긍정", 3);
    }

    public MetaDataDTO extract(String question) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Response response = openAIClient.responses().create(
                ResponseCreateParams.builder()
                        .model(ChatModel.GPT_5_1)
                        .input(promptCommandService.buildQueryMetadataPrompt(question))
                        .temperature(0)
                        .build()
        );

        String json = response.output().stream()
                .flatMap(o -> o.message().stream())
                .flatMap(m -> m.content().stream())
                .flatMap(c -> c.outputText().stream())
                .map(ResponseOutputText::text)
                .reduce("", String::concat);

        return objectMapper.readValue(json, MetaDataDTO.class);
    }



}
