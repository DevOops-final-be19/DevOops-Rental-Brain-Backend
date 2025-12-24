package com.devoops.rentalbrain.common.ai.command.service;

import com.openai.models.responses.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface AiService {
    List<Float> embed(String input);

    void indexOneDocument(String docId, String text, Map<String, Object> meta) throws IOException;

    Response answer(String q) throws IOException;
}
