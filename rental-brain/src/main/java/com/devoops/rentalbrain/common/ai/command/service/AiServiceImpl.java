package com.devoops.rentalbrain.common.ai.command.service;

import com.devoops.rentalbrain.common.ai.command.repository.OpenSearchVectorRepository;
import com.devoops.rentalbrain.common.ai.common.TextChunker;
import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.embeddings.CreateEmbeddingResponse;
import com.openai.models.embeddings.EmbeddingCreateParams;
import com.openai.models.embeddings.EmbeddingModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AiServiceImpl implements AiService {
    private final OpenAIClient openAIClient;
    private final OpenSearchVectorRepository openSearchVectorRepository;

    public AiServiceImpl(OpenAIClient openAIClient,
                         OpenSearchVectorRepository openSearchVectorRepository) {
        this.openAIClient = openAIClient;
        this.openSearchVectorRepository = openSearchVectorRepository;
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
    public void indexOneDocument(String docId, String rawText, Map<String, Object> meta) throws IOException {
        log.info("[RAG] indexOneDocument called: docId={}", docId);

        List<String> chunks = TextChunker.chunkByLength(rawText, 800); // 대충 800자 단위

        for (int i = 0; i < chunks.size(); i++) {
            String chunkText = chunks.get(i);
            List<Float> vector = embed(chunkText);

            String chunkId = docId + "_" + i;
            Map<String, Object> doc = new HashMap<>();
            doc.put("docId", docId);
            doc.put("chunkId", chunkId);
            doc.put("text", chunkText);
            doc.put("vector", vector);
            doc.put("meta", meta);
            log.info("indexOneDocument: {}", doc);

            openSearchVectorRepository.upsertChunk(chunkId, doc);
        }
    }

    public List<String> retrieveTopK(String question, int k) throws IOException {
        List<Float> qVec = embed(question);
        SearchResponse<Map> res = openSearchVectorRepository.knnSearch(qVec, k);

        List<String> texts = new ArrayList<>();
        res.hits().hits().forEach(hit -> {
            Map src = hit.source();
            if (src != null && src.get("text") != null) texts.add(String.valueOf(src.get("text")));
        });
        return texts;
    }

    public Response answer(String question) throws IOException {
        List<String> contexts = retrieveTopK(question, 5);

        String mergedContext = String.join("\n\n---\n\n", contexts);

        String input = """
                SYSTEM:
                너는 회사 내부 문서 기반 Q&A 어시스턴트다.
                아래 CONTEXT 범위 안에서만 답변하고, 근거가 없으면 "문서에서 근거를 찾지 못했습니다"라고 말해라.

                CONTEXT:
                %s

                USER QUESTION:
                %s
                """.formatted(mergedContext, question);

        ResponseCreateParams params = ResponseCreateParams.builder()
                .model(ChatModel.GPT_5_1)
                .input(input)
                .temperature(0.2)
                .build();

        return openAIClient.responses().create(params);
    }
}
