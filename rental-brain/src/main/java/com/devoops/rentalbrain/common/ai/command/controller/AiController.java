package com.devoops.rentalbrain.common.ai.command.controller;

import com.devoops.rentalbrain.common.ai.command.dto.KeywordCountDTO;
import com.devoops.rentalbrain.common.ai.command.dto.KeywordInsightDTO;
import com.devoops.rentalbrain.common.ai.common.EmbeddingDTO;
import com.devoops.rentalbrain.common.ai.command.service.AiCommandService;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseOutputText;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiController {
    private final AiCommandService aiCommandService;

    public AiController(AiCommandService aiCommandService) {
        this.aiCommandService = aiCommandService;
    }

    @PostMapping("/index")
    public void index(@RequestParam String docId, @RequestBody Map<String, Object> body) throws IOException {
        String text = (String) body.get("text");
        Map<String, Object> meta = (Map<String, Object>) body.getOrDefault("meta", Map.of());
//        aiCommandService.indexOneDocument(new EmbeddingDTO());
    }

    @GetMapping("/init/index")
    public void initIndex() throws IOException {
        aiCommandService.indexDocument();
    }

    @GetMapping("/init/index/words")
    public void initIndexWords() throws IOException {
        aiCommandService.csWordDocument();
    }

    @GetMapping("/ask")
    public String ask(@RequestParam String q) throws IOException {
        Response response = aiCommandService.answer(q);
        return response.output().stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .map(ResponseOutputText::text)
                .reduce("",(a,b)->a+b);
    }

    @GetMapping("/keyword/negative")
    public List<KeywordCountDTO> topNegative(String yearMonth) throws IOException {
        return aiCommandService.getTopNegativeKeywords(yearMonth);
    }

    @GetMapping("/keyword/positive")
    public List<KeywordCountDTO> topPositive(String yearMonth) throws IOException {
        return aiCommandService.getTopPositiveKeywords(yearMonth);
    }

    @GetMapping("/keyword/cs")
    public List<KeywordCountDTO> topCsKeywords(String yearMonth) throws IOException {
        return aiCommandService.getTopCsKeywords(yearMonth);
    }

    @GetMapping("/keyword")
    public KeywordInsightDTO getKeywordInsight(String yearMonth) throws IOException {
        return aiCommandService.getKeywordInsight(yearMonth);
    }
}
