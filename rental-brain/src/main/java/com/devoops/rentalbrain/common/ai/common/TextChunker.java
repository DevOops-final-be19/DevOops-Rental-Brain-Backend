package com.devoops.rentalbrain.common.ai.common;

import java.util.ArrayList;
import java.util.List;

public class TextChunker {
    public static List<String> chunkByLength(String text, int maxChars) {
        List<String> chunks = new ArrayList<>();
        if (text == null || text.isBlank()) return chunks;

        int start = 0;
        while (start < text.length()) {
            int end = Math.min(text.length(), start + maxChars);
            chunks.add(text.substring(start, end));
            start = end;
        }
        return chunks;
    }
}
