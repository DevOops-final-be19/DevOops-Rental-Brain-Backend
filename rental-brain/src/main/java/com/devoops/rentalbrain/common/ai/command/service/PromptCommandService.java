package com.devoops.rentalbrain.common.ai.command.service;

import java.util.List;

public interface PromptCommandService {
    String buildPrompt(List<String> ctx, String q, String style);

    String buildQueryMetadataPrompt(String question);

    String buildVocabSentimentPrompt(String text);

    String keywordExtractPrompt(String content);
}
