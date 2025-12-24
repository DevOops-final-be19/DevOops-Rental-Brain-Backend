package com.devoops.rentalbrain.common.ai.command.controller;

import com.devoops.rentalbrain.common.ai.command.service.AiService;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseOutputText;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiController {
    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/index")
    public void index(@RequestParam String docId, @RequestBody Map<String, Object> body) throws IOException {
        String text = (String) body.get("text");
        Map<String, Object> meta = (Map<String, Object>) body.getOrDefault("meta", Map.of());
        aiService.indexOneDocument(docId, text, meta);
    }

    @GetMapping("/ask")
    public String ask(@RequestParam String q) throws IOException {
        Response response = aiService.answer(q);
        String result = response.output().stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .map(ResponseOutputText::text)
                .filter(s->!s.equals("```html")&&!s.equals("```"))
                .reduce("",(a,b)->a+b);
        return result;
    }
}
